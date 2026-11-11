package ir.tadbir.app.domain.model

data class InboxItem(
    val id: String,
    val text: String,
    val createdAt: Long = System.currentTimeMillis(),
    val estimateMin: Int = 15,
    val energy: EnergyLevel = EnergyLevel.MEDIUM
)
