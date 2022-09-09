package com.example.service;

import com.google.protobuf.Descriptors;
import com.protos.Student;
import com.protos.TeacherStudentServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TeacherStudentClientService {

    @GrpcClient("grpc-example-service")
    TeacherStudentServiceGrpc.TeacherStudentServiceBlockingStub synchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getStudent(Integer studentId) {
        Student studentRequest = Student.newBuilder().setStudentId(studentId).build();
        Student studentResponse = synchronousClient.getStudent(studentRequest);
        return studentResponse.getAllFields();
    }
}
