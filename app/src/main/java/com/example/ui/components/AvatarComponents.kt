package com.example.ui.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.ui.theme.AsalPurpleDark
import com.example.ui.theme.AsalPurplePrimary
import java.io.File

/** مقادیر ویژه‌ی avatarImageUri برای عکس‌های پیش‌فرض. هر مقدار دیگری مسیر فایل عکس گالری است. */
const val AVATAR_DEFAULT_FEMALE = "default:female"
const val AVATAR_DEFAULT_MALE = "default:male"
const val GALLERY_AVATAR_COST = 10L

private val FemalePink = Color(0xFFC84FD0)
private val MaleBlue = Color(0xFF3B96D6)
private val LilacHeaderStart = Color(0xFFE9DDFB)
private val LilacHeaderEnd = Color(0xFFFCF6FF)

/**
 * آواتار واقعی کاربر: عکس گالری، پیش‌فرض خانم/آقا، یا ایموجی.
 */
@Composable
fun UserAvatar(
    imageUri: String?,
    emoji: String,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
    emojiFontSize: androidx.compose.ui.unit.TextUnit = (size.value * 0.5f).sp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when {
            imageUri == AVATAR_DEFAULT_FEMALE -> DefaultSilhouette(FemalePink, size)
            imageUri == AVATAR_DEFAULT_MALE -> DefaultSilhouette(MaleBlue, size)
            !imageUri.isNullOrBlank() -> {
                AsyncImage(
                    model = if (imageUri.startsWith("/")) File(imageUri) else imageUri,
                    contentDescription = "عکس پروفایل",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> Text(text = emoji, fontSize = emojiFontSize)
        }
    }
}

@Composable
private fun DefaultSilhouette(color: Color, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(color),
        contentAlignment = Alignment.BottomCenter
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.92f),
            modifier = Modifier.size(size * 0.82f)
        )
    }
}

/** عکس انتخاب‌شده از گالری را داخل حافظه برنامه کپی می‌کند تا همیشه در دسترس بماند. */
fun copyPickedImageToInternal(context: Context, uri: Uri): String? {
    return try {
        val dir = File(context.filesDir, "avatars").apply { mkdirs() }
        dir.listFiles()?.forEach { it.delete() }
        val out = File(dir, "avatar_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            out.outputStream().use { input.copyTo(it) }
        } ?: return null
        out.absolutePath
    } catch (_: Throwable) {
        null
    }
}

/**
 * دیالوگ «ویرایش عکس»: پیش‌فرض خانم، پیش‌فرض آقا، گالری (با کسر ۱۰ الماس).
 */
@Composable
fun EditPhotoDialog(
    currentDiamonds: Long,
    onPickDefault: (female: Boolean) -> Unit,
    onPickGallery: (path: String) -> Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        val path = copyPickedImageToInternal(context, uri)
        if (path == null) {
            Toast.makeText(context, "خواندن عکس ممکن نشد، دوباره تلاش کنید", Toast.LENGTH_SHORT).show()
        } else if (onPickGallery(path)) {
            Toast.makeText(context, "عکس پروفایل تغییر کرد ✅ (۱۰ الماس کسر شد)", Toast.LENGTH_SHORT).show()
            onDismiss()
        } else {
            Toast.makeText(context, "الماس کافی نیست 💎", Toast.LENGTH_SHORT).show()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.horizontalGradient(listOf(LilacHeaderEnd, LilacHeaderStart)))
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("📷", fontSize = 30.sp)
                        Text(
                            text = "ویرایش عکس",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F1138),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.size(30.dp))
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        PhotoOptionRowClickable(
                            title = "پیش‌فرض خانم",
                            onClick = { onPickDefault(true); onDismiss() }
                        ) { Box(Modifier.size(46.dp).clip(CircleShape)) { DefaultSilhouette(FemalePink, 46.dp) } }

                        PhotoOptionRowClickable(
                            title = "پیش‌فرض آقا",
                            onClick = { onPickDefault(false); onDismiss() }
                        ) { Box(Modifier.size(46.dp).clip(CircleShape)) { DefaultSilhouette(MaleBlue, 46.dp) } }

                        PhotoOptionRowClickable(
                            title = "گالری",
                            onClick = {
                                if (currentDiamonds < GALLERY_AVATAR_COST) {
                                    Toast.makeText(
                                        context,
                                        "برای انتخاب عکس از گالری حداقل ۱۰ الماس لازم است 💎",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    galleryLauncher.launch("image/*")
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEDE4FA)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PhotoLibrary, null, tint = AsalPurplePrimary, modifier = Modifier.size(26.dp))
                            }
                        }
                    }

                    // Info boxes
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFD9CCF2)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("با انتخاب هر عکس از گالری، ")
                                withStyle(SpanStyle(color = AsalPurplePrimary, fontWeight = FontWeight.Bold)) {
                                    append("۱۰ الماس")
                                }
                                append(" از شما کسر می‌شود")
                            },
                            fontSize = 13.sp,
                            color = Color(0xFF3B3150),
                            lineHeight = 24.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF7F0FD),
                        border = BorderStroke(1.dp, Color(0xFFEBDDF7)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("انتخاب عکس‌های نامناسب و خارج از عرف باعث ")
                                withStyle(SpanStyle(color = AsalPurplePrimary, fontWeight = FontWeight.Bold)) {
                                    append("مسدودی حساب")
                                }
                                append(" شما می‌شود")
                            },
                            fontSize = 13.sp,
                            color = Color(0xFF3B3150),
                            lineHeight = 24.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
private fun PhotoOptionRowClickable(
    title: String,
    onClick: () -> Unit,
    leading: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        leading()
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = AsalPurplePrimary
        )
    }
}

/** دیالوگ ویرایش بیوگرافی (واقعاً ذخیره می‌شود). */
@Composable
fun BioEditDialog(
    currentBio: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(currentBio) }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("✍️ بیوگرافی", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = text,
                        onValueChange = { if (it.length <= 150) text = it },
                        label = { Text("درباره من") },
                        supportingText = { Text("${text.length}/150") },
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsalPurplePrimary)
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = onDismiss) { Text("انصراف", color = Color.Gray) }
                        Button(
                            onClick = { onSave(text.trim()) },
                            enabled = text.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = AsalPurplePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("ذخیره ✅", color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}

/** دیالوگ تأیید عمومی (برای حذف حساب و ...). */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = AsalPurpleDark)
                    Spacer(Modifier.height(8.dp))
                    Text(message, fontSize = 13.sp, color = Color(0xFF4B5563), lineHeight = 22.sp)
                    Spacer(Modifier.height(14.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = onDismiss) { Text("انصراف", color = Color.Gray) }
                        Button(
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text(confirmText, color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}
