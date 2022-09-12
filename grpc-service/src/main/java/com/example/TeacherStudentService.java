package com.example;

import com.protos.Student;
import com.protos.Teacher;
import com.protos.TeacherStudentServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class TeacherStudentService extends TeacherStudentServiceGrpc.TeacherStudentServiceImplBase {

    @Override
    public void getTeacher(Teacher request, StreamObserver<Teacher> responseObserver) {
        TempDb.getTeachers()
                .stream()
                .filter(teacher -> teacher.getTeacherId() == request.getTeacherId())
                .findFirst()
                .ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void getStudentsByTeacher(Teacher request, StreamObserver<Student> responseObserver) {
        TempDb.getStudents()
                .stream()
                .filter(student -> student.getTeacherId() == request.getTeacherId())
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Student> getStudentFromTheLastCourse(StreamObserver<Student> responseObserver) {
        return new StreamObserver<>() {
            Student studentFromTheLastCourse = null;
            int courseTrack = 1;

            @Override
            public void onNext(Student student) {
                if (student.getCourse() > courseTrack) {
                    courseTrack = student.getCourse();
                    studentFromTheLastCourse = student;
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(studentFromTheLastCourse);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Student> getStudentsByTeacherSubject(StreamObserver<Student> responseObserver) {
        return new StreamObserver<>() {
            final List<Student> students = new ArrayList<>();

            @Override
            public void onNext(Student student) {
                TempDb.getStudents()
                        .stream()
                        .filter(studentFromDb -> student.getTeacherId() == studentFromDb.getTeacherId())
                        .forEach(students::add);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                students.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }

}
