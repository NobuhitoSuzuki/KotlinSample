package jp.co.sgnet.mediaPicker.internal.entity

data class CaptureStrategy(var isPublic: Boolean,
                           var authority: String = "",
                           var directiory: String? = null)