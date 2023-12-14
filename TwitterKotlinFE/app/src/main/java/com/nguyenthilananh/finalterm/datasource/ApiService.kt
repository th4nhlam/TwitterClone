import com.nguyenthilananh.finalterm.Model.IsFollowingResponse
import com.nguyenthilananh.finalterm.Model.LoginResponse
import com.nguyenthilananh.finalterm.Model.RegisterResponse
import com.nguyenthilananh.finalterm.Model.TweetAddResponse
import com.nguyenthilananh.finalterm.Model.TweetListResponse
import com.nguyenthilananh.finalterm.Model.UserFollowingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/api/v1/auth/login")
    fun login(@Query("email") email: String, @Query("password") password: String): Call<LoginResponse>
    @GET("/api/v1/auth/register")
    fun register(@Query("firstName") firstName: String, @Query("email") email: String, @Query("password") password: String, @Query("picturePath") picturePath: String): Call<RegisterResponse>
    @GET("api/v1/following/user-following")
    fun getUserFollowing(@Query("userId") userId: Int,@Query("followingUserId") followingUserId: Int,@Query("op") op: Int): Call<UserFollowingResponse>
    @GET("api/v1/following/is-following")
    fun isFollowing(@Query("userId") userId: Int,@Query("followingUserId") followingUserId: Int): Call<IsFollowingResponse>
    @GET("api/v1/tweet/add")
    fun addTweet(@Query("userId") userId: Int,@Query("tweetText") tweetText: String,@Query("tweetPicture") tweetPicture: String): Call<TweetAddResponse>
    @GET("api/v1/tweet/list")
    fun getTweetList(@Query("userId") userId: Int,@Query("op") op: Int): Call<TweetListResponse>



}
