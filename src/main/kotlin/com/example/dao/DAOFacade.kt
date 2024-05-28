package com.example.dao

import com.example.models.*


interface DAOFacade {

    //Products table functions
    suspend fun allProducts(): List<Product>
    suspend fun product(productID: Int): Product?
    suspend fun addNewProduct(
        productName: String,
        price: Float,
        imgSrc: String,
        description: String
    ): Product?
    suspend fun editProduct(
        productID: Int,
        productName: String,
        price: Float,
        imgSrc: String,
        description: String
    ): Boolean
    suspend fun deleteProduct(productID: Int): Boolean

    //Accounts table function
    suspend fun allAccounts(): List<Account>
    suspend fun account(username: String): Account?
    suspend fun addNewAccount(
        username: String,
        password: String
    ): Account?
    suspend fun changePassword(
        username: String,
        password: String
    ): Boolean
    suspend fun deleteAccount(username: String): Boolean

    //Users table function
    suspend fun allUsers(): List<User>
    suspend fun user(username: String): User?
    suspend fun addNewUser(
        name: String,
        age: Int,
        birthDay: String,
        sex: Boolean,
        phoneNumber: String,
        username: String
    ): User?
    suspend fun editUser(
        name: String,
        age: Int,
        birthDay: String,
        sex: Boolean,
        phoneNumber: String,
        username: String
    ): Boolean
    suspend fun deleteUser(
        username: String
    ): Boolean

    //Addresses table function
    suspend fun allAddresses(): List<Address>
    suspend fun addresses(userID: Int): List<Address>
    suspend fun address(userID: Int): Address?
    suspend fun addNewAddress(
        userID: Int,
        address: String
    ): Address?
    suspend fun editAddress(
        addressID: Int,
        address: String
    ): Boolean
    suspend fun deleteAddress(addressID: Int): Boolean

}