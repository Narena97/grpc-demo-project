package com.example;

import com.protos.Student;
import com.protos.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TempDb {

    public static List<Teacher> getTeachers() {
        return new ArrayList<Teacher>() {
            {
                add(Teacher.newBuilder().setTeacherId(1).setName("Sam").setSubject("Math").setStudentId(1).build());
                add(Teacher.newBuilder().setTeacherId(2).setName("Clay").setSubject("Biology").setStudentId(3).build());
                add(Teacher.newBuilder().setTeacherId(3).setName("Martin").setSubject("Chemistry").setStudentId(5).build());
            }
        };
    }

    public static List<Student> getStudents() {
        return new ArrayList<Student>() {
            {
                add(Student.newBuilder().setStudentId(1).setName("Lea").setCourse(1).setTeacherId(1).build());
                add(Student.newBuilder().setStudentId(2).setName("Maria").setCourse(2).setTeacherId(1).build());
                add(Student.newBuilder().setStudentId(3).setName("Trace").setCourse(2).setTeacherId(2).build());
                add(Student.newBuilder().setStudentId(4).setName("William").setCourse(3).setTeacherId(2).build());
                add(Student.newBuilder().setStudentId(5).setName("Lake").setCourse(3).setTeacherId(3).build());
                add(Student.newBuilder().setStudentId(6).setName("Molly").setCourse(4).setTeacherId(3).build());
            }
        };
    }

}
