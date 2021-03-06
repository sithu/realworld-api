package io.realworld.domain.profiles

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import arrow.core.toOption
import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.extensions
import arrow.effects.fix
import arrow.typeclasses.binding
import io.realworld.domain.users.User

data class GetProfileCommand(val username: String, val current: Option<User>)
data class FollowCommand(val username: String, val current: User)
data class UnfollowCommand(val username: String, val current: User)

interface GetProfileUseCase {
  val getUser: GetUserByUsername
  val hasFollower: HasFollower

  fun GetProfileCommand.runUseCase(): IO<Option<Profile>> {
    val cmd = this
    return ForIO extensions {
      binding {
        getUser(cmd.username).bind().fold(
          { none<Profile>() },
          {
            Profile(
              username = it.username,
              bio = it.bio.toOption(),
              image = it.image.toOption(),
              following = current.fold(
                { none<Boolean>() },
                { follower -> hasFollower(it.id, follower.id).bind().some() }
              )
            ).some()
          }
        )
      }.fix()
    }
  }
}

interface FollowUseCase {
  val getUser: GetUserByUsername
  val addFollower: AddFollower

  fun FollowCommand.runUseCase(): IO<Option<Profile>> {
    val cmd = this
    return ForIO extensions {
      binding {
        getUser(cmd.username).bind().fold(
          { none<Profile>() },
          {
            addFollower(it.id, cmd.current.id).bind()
            Profile(
              username = it.username,
              bio = it.bio.toOption(),
              image = it.image.toOption(),
              following = true.some()
            ).some()
          }
        )
      }.fix()
    }
  }
}

interface UnfollowUseCase {
  val getUser: GetUserByUsername
  val removeFollower: RemoveFollower

  fun UnfollowCommand.runUseCase(): IO<Option<Profile>> {
    val cmd = this
    return ForIO extensions {
      binding {
        getUser(cmd.username).bind().fold(
          { none<Profile>() },
          {
            removeFollower(it.id, cmd.current.id).bind()
            Profile(
              username = it.username,
              bio = it.bio.toOption(),
              image = it.image.toOption(),
              following = false.some()
            ).some()
          }
        )
      }.fix()
    }
  }
}
