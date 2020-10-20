package br.com.anderson.cocuscodechallenge.mapper

abstract class Mapper<in T, E> {

    abstract fun map(from: T): E
}
