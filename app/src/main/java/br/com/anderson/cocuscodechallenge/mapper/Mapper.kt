package br.com.anderson.cocuscodechallenge.mapper

import javax.inject.Singleton

@Singleton
abstract class Mapper<in T, E> {

    abstract fun map(from: T): E
}
