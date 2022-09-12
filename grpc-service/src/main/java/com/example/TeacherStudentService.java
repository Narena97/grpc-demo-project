package com.example;

import com.protos.Student;
import com.protos.Teacher;
import com.protos.TeacherStudentServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

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

}
