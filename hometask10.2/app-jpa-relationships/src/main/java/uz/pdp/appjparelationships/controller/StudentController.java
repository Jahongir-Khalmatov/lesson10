package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    GroupRepository groupRepository;


    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    //4. GROUP OWNER
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentforFaculty(@PathVariable Integer facultyId,@RequestParam int page ){
        Pageable pageable = PageRequest.of(page,10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPage;
    }
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentForGroup(@PathVariable Integer groupId,@RequestParam int page){
        Pageable pageable = PageRequest.of(page,10);
        Page<Student>studentPage = studentRepository.findAllByGroupId(groupId, pageable);
        return studentPage;
    }
    @PostMapping("/addStudent")
    public String addStudent(@RequestBody StudentDto studentDto){
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        Optional<Address> addressOptional = addressRepository.findById(studentDto.getAddressId());
        if (!addressOptional.isPresent()){
            return "address id not founded";
        }
        Address address = addressOptional.get();
        student.setAddress(address);
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()){
            return "group id not founded";
        }
        Group group = optionalGroup.get();
        student.setGroup(group);

        List<Subject> subjects = new ArrayList<>();
        List<Integer> list = studentDto.getSubjectId();
        for (Integer integer : list) {
            Optional<Subject> optionalSubject = subjectRepository.findById(integer);
        if (!optionalSubject.isPresent()){
            return "subject id not founded";
        }
            Subject subject = optionalSubject.get();
        subjects.add(subject);
            student.setSubjects(subjects);
        studentRepository.save(student);
        }
        return "student added";

    }
    @PutMapping("/edit/{id}")
    public String editStudent(@PathVariable Integer id,@RequestBody StudentDto studentDto) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()) {
            return "student id not founded";
        }
        Student student = optionalStudent.get();
        student.setLastName(studentDto.getLastName());
        student.setFirstName(studentDto.getFirstName());
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()) {
            return "group id not founded";
        }
        Group group = optionalGroup.get();
        student.setGroup(group);
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        Address address = optionalAddress.get();
        student.setAddress(address);
        List<Subject> subjectList = new ArrayList<>();
        List<Integer> list = studentDto.getSubjectId();
        for (Integer integer : list) {
            Optional<Subject> optionalSubject = subjectRepository.findById(integer);
            if (!optionalSubject.isPresent()) {
                return "subject id not founded";
            }
            Subject subject = optionalSubject.get();
            subjectList.add(subject);
            student.setSubjects(subjectList);
            studentRepository.save(student);
            break;
        }
        return "student edited";
    }

@DeleteMapping("/delete/{id}")
        public String deleteStudent(@PathVariable Integer id){
    Optional<Student> optionalStudent = studentRepository.findById(id);
    if (!optionalStudent.isPresent()){
        return "student id not founded";
    }
    Student student = optionalStudent.get();
    studentRepository.delete(student);
    return "deleted";

}







}
