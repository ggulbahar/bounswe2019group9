package app.actor.service;

import app.actor.bean.ExerciseSolvedRequest;
import app.actor.entity.ExerciseSolvedInfo;
import app.actor.repository.GradeRepository;
import app.actor.repository.SolvedExercisesRepository;
import app.common.HttpResponses;
import app.common.Response;
import app.actor.entity.User;
import app.actor.repository.UserRepository;
import app.proseidon.entity.Exercise;
import app.proseidon.repository.ContentRepository;
import app.proseidon.service.ContentService;
import java.util.List;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

/**
 * @author ahmet.gedemenli
 */

@Service
public class UserService {

  private static final String USER_NOT_FOUND = "User not found.";

  private static final String USER_SOLVED_THIS_EXERCISE_ALREADY = "User solved this exercise already.";

  private static final String EXERCISE_NOT_FOUND = "Exercise not found.";

  private final ContentService contentService;

  private final GradeRepository gradeRepository;

  private final UserRepository userRepository;

  private final SolvedExercisesRepository solvedExercisesRepository;

  private final ContentRepository contentRepository;

  public UserService(ContentService contentService, GradeRepository gradeRepository,
                     UserRepository userRepository,
                     SolvedExercisesRepository solvedExercisesRepository,
                     ContentRepository contentRepository) {
    this.contentService = contentService;
    this.gradeRepository = gradeRepository;
    this.userRepository = userRepository;
    this.solvedExercisesRepository = solvedExercisesRepository;
    this.contentRepository = contentRepository;
  }

  public Response<User> getUserById(Long id) {
    User user = userRepository.getUserById(id);
    if (isNull(user)) {
      return HttpResponses.notFound(USER_NOT_FOUND);
    }
    return HttpResponses.from(user);
  }

  public Response<ExerciseSolvedInfo> createExerciseSolvedInfo(ExerciseSolvedRequest request) {
    if (solvedExercisesRepository.getSolvedExercisesByUserId(request.getUserId()).contains(request.getExerciseId())) {
      return HttpResponses.badRequest(USER_SOLVED_THIS_EXERCISE_ALREADY);
    } else if (isNull(userRepository.getUserById(request.getUserId()))) {
      return HttpResponses.badRequest(USER_NOT_FOUND);
    } else if (isNull(contentRepository.getExerciseById(request.getExerciseId()))) {
      return HttpResponses.badRequest(EXERCISE_NOT_FOUND);
    }
    solvedExercisesRepository.createExerciseSolvedInfo(request.getUserId(), request.getExerciseId());
    Exercise exercise = contentRepository.getExerciseById(request.getExerciseId());
    Integer numberOfSolvedExercises =
        getNumberOfSolvedExercisesById(request.getUserId(), exercise.getLanguageId(), exercise.getGrade());
    Integer numberOfExercisesByGrade =
        contentService.getNumberOfExercisesByGrade(exercise.getLanguageId(), exercise.getGrade());
    if (numberOfExercisesByGrade != 0 && numberOfSolvedExercises >= numberOfExercisesByGrade) {
      gradeRepository.addGrade(request.getUserId(), exercise.getLanguageId(), exercise.getGrade() + 1);
    }
    return HttpResponses.successful();
  }

  public Integer getNumberOfSolvedExercisesById(Long userId, Integer langId, Integer grade) {
    List<ExerciseSolvedInfo> solveds = solvedExercisesRepository.getSolvedExercisesByUserId(userId);
    Integer counter = 0;
    for (ExerciseSolvedInfo solved : solveds) {
      if (contentRepository.getExerciseById(solved.getExerciseId()).getGrade() != grade
          || contentRepository.getExerciseById(solved.getExerciseId()).getLanguageId() != langId) {
        continue;
      }
      counter++;
    }
    return counter;
  }
}
