package com.example.controller;

import com.example.service.TeacherStudentClientService;
import com.google.protobuf.Descriptors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TeacherStudentController {

    private final TeacherStudentClientService teacherStudentService;

    @GetMapping("/student/{studentId}")
    public Map<Descriptors.FieldDescriptor, Object> getStudent(@PathVariable String studentId) {
        return teacherStudentService.getStudent(Integer.parseInt(studentId));
    }
}
