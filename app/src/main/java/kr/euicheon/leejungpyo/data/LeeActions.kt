package kr.euicheon.leejungpyo.data

/**
 * Describes the various actions which can be triggered from within the Calpose renderer
 *
 * @property onClickedPreviousMonth Called upon pressing the previous month button
 *
 * @property onClickedNextMonth Called upon pressing the next month button
 *
 * @property onSwipedPreviousMonth Called upon swiping towards the previous month
 *
 * @property onSwipedNextMonth Called upon swiping towards the next month
 */
data class LeeActions (
    val onClickedPreviousMonth: () -> Unit,
    val onClickedNextMonth: () -> Unit,
    val onSwipedPreviousMonth: () -> Unit = onClickedPreviousMonth,
    val onSwipedNextMonth: () -> Unit = onClickedNextMonth
)