package com.example.controller;

import com.example.service.TeacherStudentClientService;
import com.google.protobuf.Descriptors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TeacherStudentController {

    private final TeacherStudentClientService teacherStudentService;

    @GetMapping("/teacher/{teacherId}")
    public Map<Descriptors.FieldDescriptor, Object> getTeacher(@PathVariable String teacherId) {
        return teacherStudentService.getTeacher(Integer.parseInt(teacherId));
    }

    @GetMapping("/student/{teacherId}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getStudentsByTeacher(@PathVariable String teacherId) throws InterruptedException {
        return teacherStudentService.getStudentsByTeacher(Integer.parseInt(teacherId));
    }

}
