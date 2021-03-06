package app.mahmuthoca.service;

import app.actor.service.UserService;
import app.common.HttpResponses;
import app.common.Response;
import app.mahmuthoca.bean.CreateRatingRequest;
import app.mahmuthoca.bean.UpdateRatingRequest;
import app.mahmuthoca.entity.Rating;
import app.mahmuthoca.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * @author ahmet.gedemenli
 */

@Service
public class RatingService {

  private static final String INVALID_USER_ID = "Invalid user id.";

  private static final String RATING_OUT_OF_RANGE = "Rating should be between 1-5";

  private static final String RATING_EXISTS = "Rating already exists.";

  private static final String NO_RATING = "No rating between.";

  private final UserService userService;

  private final RatingRepository ratingRepository;

  public RatingService(UserService userService, RatingRepository ratingRepository) {
    this.userService = userService;
    this.ratingRepository = ratingRepository;
  }

  public Response<CreateRatingRequest> addRating(CreateRatingRequest request) {
    if (request.getRating() < 1 || request.getRating() > 5) {
      return HttpResponses.badRequest(RATING_OUT_OF_RANGE);
    }
    if (isNull(userService.getUserById(request.getReceiverId()).getData()) ||
        isNull(userService.getUserById(request.getSenderId()).getData())) {
      return HttpResponses.badRequest(INVALID_USER_ID);
    }
    List<Rating> userRatings = ratingRepository.getRatingsByReceiverId(request.getReceiverId());
    for (Rating rating : userRatings) {
      if (rating.getSenderId() == request.getSenderId()) {
        return HttpResponses.badRequest(RATING_EXISTS);
      }
    }
    ratingRepository.addRating(request.getSenderId(), request.getReceiverId(), request.getRating());
    return HttpResponses.from(request);
  }

  public Double getAverageRatingByUserId(Long userId) {
    List<Rating> userRatings = ratingRepository.getRatingsByReceiverId(userId);
    if (userRatings.size() == 0) {
      return 0.0;
    }
    Double sum = 0.0;
    for (Rating rating : userRatings) {
      sum += rating.getRating();
    }
    return sum / userRatings.size();
  }

  public Response<Integer> getRatingBetween(Long senderId, Long receiverId) {
    if (isNull(ratingRepository.getRatingBetween(senderId, receiverId))) {
      return HttpResponses.badRequest(NO_RATING);
    }
    return HttpResponses.from(ratingRepository.getRatingBetween(senderId, receiverId).getRating());
  }

  public Response<Integer> updateRating(UpdateRatingRequest request) {

    if (request.getRating() < 1 || request.getRating() > 5) {
      return HttpResponses.badRequest(RATING_OUT_OF_RANGE);
    }


    if (isNull(userService.getUserById(request.getReceiverId()).getData()) ||
            isNull(userService.getUserById(request.getSenderId()).getData())) {
      return HttpResponses.badRequest(INVALID_USER_ID);
    }

    /*List<Rating> userRatings = ratingRepository.getRatingsByReceiverId(request.getReceiverId());
    for (Rating rating : userRatings) {
      if (rating.getSenderId() == request.getSenderId()) {
        break;
        return HttpResponses.badRequest(RATING_EXISTS);
      }
    }*/

    ratingRepository.updateRating(request.getSenderId(), request.getReceiverId(), request.getRating());
    return getRatingBetween(request.getSenderId(), request.getReceiverId());

  }

}
