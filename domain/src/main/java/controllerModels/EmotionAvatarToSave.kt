package controllerModels

import Event
import org.springframework.web.multipart.MultipartFile

data class EmotionAvatarToSave(val imageFile: MultipartFile, val givenEvent: Event)
