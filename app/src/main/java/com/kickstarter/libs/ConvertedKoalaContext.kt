package com.kickstarter.libs

sealed class ConvertedKoalaContext {
  /**
   * Determines the place from which the comments dialog was presented.
   *
   * PROJECT_ACTIVITY: The creator's project activity screen.
   * PROJECT_COMMENTS: The comments screen for a project.
   * UPDATE_COMMENTS:  The comments screen for an update.
   */
  sealed class CommentDialog {
    object PROJECT_ACTIVITY : CommentDialog()
    object PROJECT_COMMENTS : CommentDialog()
    object UPDATE_COMMENTS : CommentDialog()

    fun trackingString(): String = when(this) {
      is PROJECT_ACTIVITY -> "project_activity"
      is PROJECT_COMMENTS -> "project_comments"
      is UPDATE_COMMENTS -> "update_comments"
    }
  }

  /**
   * Determines the place from which the comments were presented.
   *
   * PROJECT: The comments for a project.
   * UPDATE:  The comments for an update.
   */
  sealed class Comments {
    object PROJECT : Comments()
    object UPDATE : Comments()

    fun trackingString(): String = when(this) {
      is PROJECT -> "project"
      is UPDATE -> "update"
    }
  }


  /**
   * Determines the place from which the Update was presented.
   *
   * UPDATES:           The updates index.
   * ACTIVITY:          The activity feed.
   * ACTIVITY_SAMPLE:   The activity sample.
   */
  sealed class Update {
    object UPDATES : Update()
    object ACTIVITY : Update()
    object ACTIVITY_SAMPLE : Update()

    fun trackingString(): String = when(this) {
      is UPDATES -> "updates"
      is ACTIVITY -> "activity"
      is ACTIVITY_SAMPLE -> "activity_sample"
    }
  }
}
