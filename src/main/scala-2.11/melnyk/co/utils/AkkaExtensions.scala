package melnyk.co.utils

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.twitter.util.{Future => TwitterFuture}
import com.twitter.bijection.Conversion._
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture


import scala.concurrent.{ExecutionContextExecutor, Future => ScalaFuture}
import scala.concurrent.duration._

trait AkkaExtensions {
  implicit val timeout = Timeout(10.seconds)

  def ->[T](in: ScalaFuture[T])(implicit executionContext:ExecutionContextExecutor): TwitterFuture[T] = in.as[TwitterFuture[T]]

  def -?>(actor:ActorRef, message: Any)(implicit timeout: Timeout, sender: ActorRef = Actor.noSender, executionContext:ExecutionContextExecutor): TwitterFuture[Any] =
    actor.?(message).as[TwitterFuture[Any]]

  def -!>(actor:ActorRef, message: Any)(implicit timeout: Timeout, sender: ActorRef = Actor.noSender, executionContext:ExecutionContextExecutor): TwitterFuture[Any] =
    TwitterFuture (actor.!(message))
}
