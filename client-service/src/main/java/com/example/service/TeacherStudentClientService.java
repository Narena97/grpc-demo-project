package com.example.service;

import com.google.protobuf.Descriptors;
import com.protos.Student;
import com.protos.Teacher;
import com.protos.TeacherStudentServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class TeacherStudentClientService {

    @GrpcClient("grpc-example-service")
    TeacherStudentServiceGrpc.TeacherStudentServiceBlockingStub synchronousClient;

    @GrpcClient("grpc-example-service")
    TeacherStudentServiceGrpc.TeacherStudentServiceStub asynchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getTeacher(Integer teacherId) {
        Teacher teacherRequest = Teacher.newBuilder().setTeacherId(teacherId).build();
        Teacher teacherResponse = synchronousClient.getTeacher(teacherRequest);
        return teacherResponse.getAllFields();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getStudentsByTeacher(Integer teacherId) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Teacher teacherRequest = Teacher.newBuilder().setTeacherId(teacherId).build();
        List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();

        asynchronousClient.getStudentsByTeacher(teacherRequest, new StreamObserver<>() {
            @Override
            public void onNext(Student student) {
                response.add(student.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

}
