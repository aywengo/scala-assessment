package melnyk.co.utils

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.twitter.util.{Future => TwitterFuture}
import com.twitter.bijection.Conversion._
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import scala.concurrent.{Future => ScalaFuture}
import scala.concurrent.ExecutionContext.Implicits.global

trait AkkaExtensions {
  def ->[T](in: ScalaFuture[T]): TwitterFuture[T] = in.as[TwitterFuture[T]]

  def ->(actor:ActorRef, message: Any)(implicit timeout: Timeout, sender: ActorRef = Actor.noSender): TwitterFuture[Any] =
    actor.?(message).as[TwitterFuture[Any]]
}
