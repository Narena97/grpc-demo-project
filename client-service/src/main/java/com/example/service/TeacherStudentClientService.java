package com.example.service;

import com.example.TempDb;
import com.google.protobuf.Descriptors;
import com.protos.Student;
import com.protos.Teacher;
import com.protos.TeacherStudentServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getStudentFromTheLastCourse() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Map<String, Map<Descriptors.FieldDescriptor, Object>> response = new HashMap<>();
        StreamObserver<Student> responseObserver = asynchronousClient.getStudentFromTheLastCourse(new StreamObserver<>() {
            @Override
            public void onNext(Student student) {
                response.put("StudentFromTheLastCourse", student.getAllFields());
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

        TempDb.getStudents().forEach(responseObserver::onNext);
        responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyMap();
    }

}
