package raisetech.student.management.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.model.data.Student;
import raisetech.student.management.model.data.StudentCourse;
import raisetech.student.management.model.domain.StudentDetail;
import raisetech.student.management.model.services.StudentService;

@Controller
public class StudentController {

  private final StudentService service;
  private final StudentConverter converter;
  private List<StudentDetail> studentsDetails; // コントローラーのフィールドとして空のstudentDetailを定義

  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/students")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentsCourses = service.searchStudentsCourseList();
    studentsDetails = converter.convertStudentDetails(students, studentsCourses); // フィールドに設定する

    model.addAttribute("studentList", studentsDetails);
    return "studentList";
  }

  @GetMapping("/students-courses")
  public String getStudentCourseList(Model model) {
    List<StudentCourse> studentsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentCourseList", studentsCourses);
    return "studentCourseList"; // studentCourseList.htmlを指す。
  }

  @GetMapping("/students/new")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @GetMapping("/students/update")
  // 更新ボタンをクリックされたときのリンクに含まれる生徒のID情報を使ってstudentsDetailsから該当する生徒情報を取得し、その生徒情報に紐づく更新フォームを表示する
  public String updateStudent(@RequestParam("id") Long id, Model model) {
    StudentDetail studentDetail = studentsDetails.stream()
        .filter(detail -> detail.getStudent().getId() == id)
        .findFirst().orElse(null);

    if (studentDetail != null) {
      model.addAttribute("studentDetail", studentDetail);
      return "updateStudent";
    } else {
      return "redirect:/students";
    }
  }

  @PostMapping("/students/new")
  // ビューの登録フォームで入力されたstudentDetailの情報をstudentServiceに送る
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }

    // フォームで入力されたstudentDetailのstudentの情報とstudentCourseの情報（最初の一つ）をserviceにあるregisterStudentメソッドの引数とする
    service.registerStudent(studentDetail);
    return "redirect:/students";
  }

  @PostMapping("/students/update")
  // ビューの更新フォームで入力されたstudentDetailの情報をstudentServiceに送る
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result,
      // name="_method"を指定して、PUTリクエストを受けとり、引数methodに入れる
      @RequestParam(name = "_method", required = false) String method) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    // methodがPUTリクエストなら、service.updateStudentメソッドを実行する。
    // フォームで更新されたstudentDetailをserviceにあるupdateStudentメソッドの引数とする
    if ("PUT".equals(method)) {
      service.updateStudent(studentDetail);
    }
    return "redirect:/students";
  }
}
