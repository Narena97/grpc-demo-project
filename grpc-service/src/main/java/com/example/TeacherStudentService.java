package com.example;

import com.protos.Student;
import com.protos.TeacherStudentServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TeacherStudentService extends TeacherStudentServiceGrpc.TeacherStudentServiceImplBase {

    @Override
    public void getStudent(Student request, StreamObserver<Student> responseObserver) {
        TempDb.getStudents()
                .stream()
                .filter(student -> student.getStudentId() == request.getStudentId())
                .findFirst()
                .ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }

}
