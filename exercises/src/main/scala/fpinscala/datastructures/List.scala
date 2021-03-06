package fpinscala.datastructures
//import scala.collection

sealed trait List[+A] // `List` data type, parameterized on a type, `A`
case object Nil extends List[Nothing] // A `List` data constructor representing the empty list
/* Another data constructor, representing nonempty lists. Note that `tail` is another `List[A]`,
which may be `Nil` or another `Cons`.
 */
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List { // `List` companion object. Contains functions for creating and working with lists.
  def sum(ints: List[Int]): Int = ints match { // A function that uses pattern matching to add up a list of integers
    case Nil => 0 // The sum of the empty list is 0.
    case Cons(x,xs) => x + sum(xs) // The sum of a list starting with `x` is `x` plus the sum of the rest of the list.
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x,xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] = // Variadic function syntax
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  val x = List(1,2,3,4,5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match {
      case Nil => a2
      case Cons(h,t) => Cons(h, append(t, a2))
    }

  def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B = // Utility functions
    as match {
      case Nil => z
      //case Cons(0.0, xs) => 0.0
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sum2(ns: List[Int]) =
    foldRight(ns, 0)((x,y) => x + y)

  def product2(ns: List[Double]) =
    foldRight(ns, 1.0)(_ * _) // `_ * _` is more concise notation for `(x,y) => x * y`; see sidebar

  def tail[A](l: List[A]): List[A] = l match {
    case Nil => throw new UnsupportedOperationException("tail of Nil list")
    //case Nil => Nil
    case Cons(x, xs) => xs
  }

  def setHead[A](l: List[A], h: A): List[A] = l match {
    case Nil => Nil
    case Cons(_, xs) => Cons(h, xs) 
  }

  def drop[A](l: List[A], n: Int): List[A] = {
    if (n<=0) l
    else l match {
      case Nil if (n>0) => throw new UnsupportedOperationException("drop count bigger than list length")
      case Cons(x,xs) => drop(xs,n-1)
    }
  }

  // def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
  //   case Nil => Nil
  //   case Cons(x,xs) =>
  //     if (f(x))
  //       dropWhile(xs, f)
  //     else
  //       l
  // }

  def dropWhile[A](l: List[A])(f: A => Boolean): List[A] = l match {
    case Cons(h, t) if f(h) => dropWhile(t)(f)
    case _ => l
  }

  def init[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(_,Nil) => Nil
    case Cons(x, xs) => Cons(x, init(xs))
  }

  def length[A](l: List[A]): Int = {
    foldRight(l, 0)((x,y) => y + 1)
  }

  def foldLeft[A,B](l: List[A], z: B)(f: (B, A) => B): B = {
    @annotation.tailrec
    def go(l: List[A], acc: B)(f: (B, A) => B): B = l match {
      case Nil => acc
      //case Cons(0, as) => 0
      case Cons(a, as) => go(as, f(acc, a))(f)
    }

    go(l, z)(f)
  }

  def sum3(ns: List[Int]) =
    foldLeft(ns, 0)(_ + _)

  def product3(ns: List[Int]) =
    foldLeft(ns, 1.0)(_ * _) // `_ * _` is more concise notation for `(x,y) => x * y`; see sidebar

  def length2[A](l: List[A]): Int = {
    foldLeft(l, 0)((x,y) => x + 1)
  }

  def reverse[A](l: List[A]): List[A] = {
    @annotation.tailrec
    def go[A](l: List[A], buff: List[A]): List[A] = l match {
      case Nil => buff
      case Cons(a, as) => go(as, Cons(a, buff))
    }

    go(l, Nil)
  }

  def reverse2[A](l: List[A]): List[A] = {
    foldLeft(l, List[A]())((acc, a)=>Cons(a, acc))
    //foldLeft(l, Nil)((acc, a)=>Cons(a, acc))  
  }

  def append2[A](a1: List[A], a2: List[A]): List[A] = foldRight(a1, a2)(Cons.apply)

  def concatenate[A](ls: List[A]*): List[A] = {
    if (ls.isEmpty) Nil
    else List.append2(ls.head, concatenate(ls.tail: _*))
  }

  // def add1[A](l: List[A]): List[A] = l match {
  //   case Nil => Nil
  //   case Cons(x,xs) => foldRight()
  // }

  def add1[A](l: List[Int]): List[Int] = foldRight(l, List[Int]())((x, y) => Cons(x + 1, y))
 
  def listDblToStr[A](l: List[Double]): String = foldLeft(l, "")((acc, a) => acc + "*" + a.toString)

  def listDblToListStr[A](l: List[Double]): List[String] = foldRight(l, List[String]())((x, xs) => Cons(x.toString, xs))
  
  def map[A,B](l: List[A])(f: A => B): List[B] = foldRight(l, List[B]())((h,t) => Cons(f(h), t))

  def filter[A](as: List[A])(f: A => Boolean): List[A] = foldRight(as, List[A]())((h,t) => {if (f(h)) Cons(h, t) else t})

  def flatMap[A,B](l: List[A])(f: A => List[B]): List[B] = foldRight(l, List[B]())((h, t) => append2(f(h), t))

  //def flatMap2[A,B](l: List[A])(f: A => List[B]): List[B] = concatenate(map(l)(f))

  def filter2[A](l: List[A])(f: A => Boolean): List[A] = flatMap(l)(x => if (f(x)) Cons(x, Nil) else List[A]())

  def addL(a: List[Int], b: List[Int]): List[Int] = a match {
    case Nil => Nil
    case Cons(x, xs) => b match {
      case Nil => Nil
      case Cons(y, ys) => Cons(x+y, addL(xs, ys))
    } 
  }

  def zipWith[A,B](a: List[A], b: List[A])(f: (A, A) => B): List[B] = (a, b) match {
    case (_, Nil) => Nil
    case (Nil, _) => Nil
    case (Cons(x, xs), Cons(y, ys)) => Cons(f(x,y), zipWith(xs, ys)(f))
  }

  // @annotation.tailrec
  // def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = (sup,sub) match {
  //   case (_, Nil) => true
  //   case (Nil, _) => false
  //   case (Cons(x,xs), Cons(y,ys)) => ((x==y) && hasSubsequence(xs,ys)) || hasSubsequence(xs,sub)
  // }

  @annotation.tailrec
  def startsWith[A](a: List[A], b: List[A]): Boolean = (a,b) match {
    case (_,Nil) => true
    case (Cons(x,xs), Cons(y,ys)) if (x==y) => startsWith(xs,ys)
    case _ => false 
  }

   @annotation.tailrec
   def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = (sup,sub) match {
     case (_,Nil) => true
     case (Nil,_) => false
     case (_,_) if startsWith(sup,sub) => true
     case (Cons(x,xs), _) => hasSubsequence(xs,sub) 
   }

}

