package app.proseidon.repository;

import app.proseidon.entity.Content;
import app.proseidon.entity.Exercise;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author ahmet.gedemenli
 */

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {

  @Query(value = "SELECT * FROM proseidon.exercises", nativeQuery = true)
  List<Exercise> getAllExercises();

  @Query(value = "SELECT * FROM proseidon.exercises WHERE language_id=:languageId LIMIT 10", nativeQuery = true)
  List<Exercise> getProficiencyExam(@Param("languageId") Integer languageId);

  @Transactional
  @Modifying
  @Query(value = "INSERT INTO proseidon.exercises(language_id, type_id, grade, image_url, sound_url," +
                 " question_body, option_a, option_b, option_c, option_d, correct_answer)" +
                 "VALUES(:languageId, :typeId, :grade, :imageUrl, :soundUrl, :questionBody, :optionA, :optionB," +
                 " :optionC, :optionD, :correctAnswer) ", nativeQuery = true)
  void addExercise(@Param("languageId") Integer languageId, @Param("typeId") Integer typeId,
                   @Param("grade") Integer grade,
                   @Param("imageUrl") String imageUrl, @Param("soundUrl") String soundUrl,
                   @Param("questionBody") String questionBody, @Param("optionA") String optionA,
                   @Param("optionB") String optionB, @Param("optionC") String optionC,
                   @Param("optionD") String optionD, @Param("correctAnswer") Integer correctAnswer);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM proseidon.exercises WHERE id=:id", nativeQuery = true)
  void deleteExercise(@Param("id") Long id);

  @Query(value = "SELECT * FROM proseidon.exercises WHERE id=:id", nativeQuery = true)
  Exercise getExerciseById(@Param("id") Long id);

  @Query(value = "SELECT count(*) FROM proseidon.exercises WHERE language_id=:langId AND grade=:grade",
         nativeQuery = true)
  Integer getNumberOfExercisesByGrade(@Param("langId") Integer langId, @Param("grade") Integer grade);

  @Transactional
  @Modifying
  @Query(value = "UPDATE proseidon.exercises SET image_url = :url WHERE id = :id", nativeQuery = true)
  void upsertImageUrl(@Param("url") String url, @Param("id") Long id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE proseidon.exercises SET sound_url = :url WHERE id = :id", nativeQuery = true)
  void upsertSoundUrl(@Param("url") String url, @Param("id") Long id);

  @Query(value = "SELECT id FROM proseidon.exercises ORDER BY id DESC LIMIT 1",nativeQuery = true)
  Long getLastExerciseId();
}
