package com.medioambiente.network

import com.medioambiente.models.*
import okhttp3.ResponseBody
import retrofit2.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @GET("noticias/{id}")
    suspend fun getNoticiaById(@Path("id") id: String): Response<News>

    @GET("noticias")
    fun getNoticias(): retrofit2.Call<List<News>>

    @GET("servicios")
    suspend fun getServicios(): Response<List<Servicio>>

    @GET("videos")
    fun getVideos(): retrofit2.Call<List<Video>>

    @GET("medidas")
    fun getMedidas(): retrofit2.Call<List<Medida>>

    @GET("equipo")
    fun getEquipo(): retrofit2.Call<List<Miembro>>

    @GET("normativas")
    fun getNormativas(): retrofit2.Call<List<Normativa>>

    @GET("areas_protegidas")
    suspend fun getAreasProtegidas(
        @Query("tipo") tipo: String? = null,
        @Query("busqueda") busqueda: String? = null
    ): Response<List<AreaProtegida>>

    @FormUrlEncoded
    @POST("voluntarios")
    suspend fun solicitarVoluntariado(
        @Field("cedula") cedula: String,
        @Field("nombre") nombre: String,
        @Field("apellido") apellido: String,
        @Field("correo") correo: String,
        @Field("password") password: String,
        @Field("telefono") telefono: String
    ): Response<VoluntarioResponse>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("correo") correo: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("cedula") cedula: String,
        @Field("nombre") nombre: String,
        @Field("apellido") apellido: String,
        @Field("correo") correo: String,
        @Field("password") password: String,
        @Field("telefono") telefono: String,
        @Field("matricula") matricula: String
    ): Response<RegisterResponse>

    @POST("auth/reset")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse>

    @Multipart
    @POST("reportes")
    suspend fun sendReporte(
        @Header("Authorization") token: String,
        @Part("titulo") titulo: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("foto") foto: RequestBody,
        @Part("latitud") latitud: RequestBody,
        @Part("longitud") longitud: RequestBody
    ): Response<ResponseBody>

    @GET("reportes")
    suspend fun getMisReportes(
        @Header("Authorization") token: String
    ): Response<List<Reporte>>

    @GET("reportes/{id}")
    suspend fun getReporteById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Reporte>

    @FormUrlEncoded
    @POST("auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Field("current_password") currentPassword: String,
        @Field("new_password") newPassword: String
    ): Response<ApiResponse>

    @POST("auth/recover")
    suspend fun forgotPassword(@Body request: EmailRequest): Response<RecoverPasswordResponse>
}