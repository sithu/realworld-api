package io.realworld.domain.articles

import arrow.core.Either
import arrow.core.Option
import arrow.effects.IO
import io.realworld.domain.users.User
import java.util.UUID

typealias CreateArticle = (article: ValidArticleCreation, user: User) -> IO<Article>

typealias ValidateArticleUpdate = (update: ArticleUpdate, slug: String, user: User) ->
  IO<Either<ArticleUpdateError, ValidArticleUpdate>>
typealias UpdateArticle = (update: ValidArticleUpdate, user: User) -> IO<Article>

typealias CreateUniqueSlug = (title: String) -> IO<String>
typealias ExistsBySlug = (slug: String) -> IO<Boolean>

typealias GetArticleBySlug = (slug: String, user: Option<User>) -> IO<Option<Article>>

typealias DeleteArticle = (id: UUID) -> IO<Int>

typealias AddFavorite = (articleId: UUID, user: User) -> IO<Int>
typealias RemoveFavorite = (articleId: UUID, user: User) -> IO<Int>

typealias AddComment = (articleId: UUID, comment: String, user: User) -> IO<Comment>
typealias DeleteComment = (id: Long) -> IO<Int>
typealias GetComment = (id: Long, user: User) -> IO<Option<Comment>>
typealias GetComments = (articleId: UUID, user: Option<User>) -> IO<List<Comment>>
