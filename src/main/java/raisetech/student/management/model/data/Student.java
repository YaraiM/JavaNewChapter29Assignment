package raisetech.student.management.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生情報
 */
@Getter
@Setter
public class Student {

  private int id;
  private String fullname;
  private String furigana;
  private String nickname;
  private String mail;
  private String address;
  private Integer age; // Spring Boot（Java）では、int型フィールドは初期値0となり、Nullが許容されない
  private Gender gender; //Javaでenum型を取り扱う場合、別途定義する必要がある
  private String remark;
  @JsonProperty("isDeleted") // JSONシリアライザー（Jackson）でbooleanフィールド名が自動調整されることを防ぐ
  private boolean isDeleted;

}
