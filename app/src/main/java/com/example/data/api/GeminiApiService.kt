package com.example.data.api

import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

// --- Request Data Models for Gemini REST API ---
data class GeminiPart(
    val text: String? = null,
    val inlineData: InlineDataPart? = null
)

data class InlineDataPart(
    val mimeType: String,
    val data: String
)

data class GeminiContent(
    val role: String? = "user",
    val parts: List<GeminiPart>
)

data class GeminiGenerationConfig(
    val temperature: Float? = 0.4f,
    val topP: Float? = 0.95f,
    val topK: Int? = 40
)

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig? = GeminiGenerationConfig()
)

// --- Response Data Models ---
data class GeminiCandidate(
    val content: GeminiContent?
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApi::class.java)
    }

    private fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    suspend fun analyzeCostumeImage(bitmap: Bitmap, promptExtra: String = ""): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key not configured in Secrets. Please add GEMINI_API_KEY in the Secrets panel."
        }

        val promptText = """
            You are an expert AI Costume & Fashion Recognition System for ThreadCraft 3D.
            Analyze the provided costume/outfit photo or sketch carefully and perform image recognition.
            
            Extract and report in a clear structured format:
            1. DETECTED STYLE: (e.g. Cyberpunk, Royal Fantasy, Steampunk, Cosplay, Victorian, Streetwear)
            2. PRIMARY FABRIC / MATERIAL: (e.g. Velvet, Silk Satin, Grain Leather, Gold Brocade, Denim, Lace)
            3. COLOR PALETTE: (List 3-4 dominant colors and hex codes if possible)
            4. THEME & ERA: (e.g. Futuristic 2099, 18th Century Elizabethan, Modern)
            5. MATCH CONFIDENCE SCORE: (0-100%)
            6. MATCHING INVENTORY SUGGESTIONS: (Identify which inventory items like Velvet Rolls, Gold Brocade, Leather Sheeting, Brass Buckles, or Silk Bolts match this design best)
            7. 3D CUSTOMIZATION RECOMMENDATION: (Brief suggestion for how to build this in ThreadCraft 3D Studio)
            
            $promptExtra
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPart(text = promptText),
                        GeminiPart(inlineData = InlineDataPart(mimeType = "image/jpeg", data = bitmap.toBase64()))
                    )
                )
            )
        )

        try {
            val response = api.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull { it.text != null }?.text
            text ?: "No analysis text generated."
        } catch (e: Exception) {
            "Error performing AI costume image analysis: ${e.localizedMessage}"
        }
    }

    suspend fun generateFashionAdvice(userQuery: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key not configured in Secrets. Please configure GEMINI_API_KEY in Secrets."
        }

        val promptText = """
            You are Atelier AI, an elite 3D Costume Master and Fashion Design Copilot.
            Answer the user's costume design query with tailored fashion advice, fabric selection tips, drape and texture recommendations, and 3D modeling advice.
            
            User Question: $userQuery
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(text = promptText))
                )
            )
        )

        try {
            val response = api.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull { it.text != null }?.text
            text ?: "Atelier AI could not generate a response."
        } catch (e: Exception) {
            "Atelier AI Error: ${e.localizedMessage}"
        }
    }
}
