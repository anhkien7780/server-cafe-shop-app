package com.cafeshop.dao

import com.example.CafeShopDatabase.dbQuery
import com.example.dao.DAOFacade
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToProduct(row: ResultRow) = Product(
        productID = row[Products.productID],
        productName = row[Products.productName],
        price = row[Products.price],
        imgSrc = row[Products.imgSrc],
        description = row[Products.description]
    )

    private fun resultRowToAccount(row: ResultRow) = Account(
        username = row[Accounts.username],
        password = row[Accounts.password]
    )

    private fun resultRowToUser(row: ResultRow) = User(
        name = row[Users.name],
        age = row[Users.age],
        birthDay = row[Users.birthDay],
        sex = row[Users.sex],
        phoneNumber = row[Users.phoneNumber],
        username = row[Users.username]
    )


    private fun resultRowToAddress(row: ResultRow) = Address(
        addressID = row[Addresses.addressID],
        address = row[Addresses.address],
        userID = row[Addresses.userID],
    )


    // PRODUCT
    override suspend fun allProducts(): List<Product> = dbQuery {
        Products.selectAll().map(::resultRowToProduct)
    }

    override suspend fun product(productID: Int): Product? = dbQuery {
        Products
            .select { Products.productID eq productID }
            .map(::resultRowToProduct)
            .singleOrNull()
    }

    override suspend fun addNewProduct(
        productName: String,
        price: Float,
        imgSrc: String,
        description: String
    ): Product? = dbQuery {
        val insertStatement = Products.insert {
            it[Products.productName] = productName
            it[Products.price] = price
            it[Products.imgSrc] = imgSrc
            it[Products.description] = description
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToProduct)
    }

    override suspend fun editProduct(
        productID: Int,
        productName: String,
        price: Float,
        imgSrc: String,
        description: String
    ): Boolean = dbQuery {
        Products.update({ Products.productID eq productID }) {
            it[Products.productName] = productName
            it[Products.price] = price
            it[Products.imgSrc] = imgSrc
            it[Products.description] = description
        } > 0
    }

    override suspend fun deleteProduct(productID: Int): Boolean = dbQuery {
        Products.deleteWhere { Products.productID eq productID } > 0
    }

    // ACCOUNT
    override suspend fun allAccounts(): List<Account> = dbQuery {
        Accounts.selectAll().map(::resultRowToAccount)
    }

    override suspend fun account(username: String): Account? = dbQuery {
        Accounts
            .select { Accounts.username eq username }
            .map(::resultRowToAccount)
            .singleOrNull()
    }

    override suspend fun addNewAccount(username: String, password: String): Account? = dbQuery {
        val insertStatement = Accounts.insert {
            it[Accounts.username] = username
            it[Accounts.password] = password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAccount)
    }

    override suspend fun changePassword(username: String, password: String): Boolean = dbQuery {
        Accounts.update({ Accounts.username eq username }) {
            it[Accounts.password] = password
        } > 0
    }

    override suspend fun deleteAccount(username: String): Boolean = dbQuery {
        Accounts.deleteWhere { Accounts.username eq username } > 0
    }

    // USER
    override suspend fun allUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    override suspend fun user(username: String): User? = dbQuery {
        Users
            .select{Users.username eq username}
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun addNewUser(
        name: String,
        age: Int,
        birthDay: String,
        sex: Boolean,
        phoneNumber: String,
        username: String
    ): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.name] = name
            it[Users.age] = age
            it[Users.birthDay] = birthDay
            it[Users.sex] = sex
            it[Users.phoneNumber] = phoneNumber
            it[Users.username] = username
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun editUser(
        name: String,
        age: Int,
        birthDay: String,
        sex: Boolean,
        phoneNumber: String,
        username: String
    ): Boolean = dbQuery {
        Users.update({Users.username eq username}){
            it[Users.name] = name
            it[Users.age] = age
            it[Users.birthDay] = birthDay
            it[Users.sex] = sex
            it[Users.phoneNumber] = phoneNumber
            it[Users.username] = username
        } > 0
    }

    override suspend fun deleteUser(username: String): Boolean = dbQuery {
        Users.deleteWhere { Users.username eq username } > 0
    }

    // ADDRESS
    override suspend fun allAddresses(): List<Address> = dbQuery {
        Addresses.selectAll().map(::resultRowToAddress)
    }

    override suspend fun addresses(userID: Int): List<Address> = dbQuery {
        Addresses
            .select{Addresses.userID eq userID}
            .map(::resultRowToAddress)
    }

    override suspend fun address(userID: Int): Address? = dbQuery {
        Addresses
            .select { Addresses.userID eq userID }
            .map(::resultRowToAddress)
            .singleOrNull()
    }

    override suspend fun addNewAddress(userID: Int, address: String): Address? = dbQuery {
        val insertStatement = Addresses.insert {
            it[Addresses.userID] = userID
            it[Addresses.address] = address
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAddress)
    }

    override suspend fun editAddress(addressID: Int, address: String): Boolean = dbQuery {
        Addresses
            .update({Addresses.addressID eq addressID}){
                it[Addresses.address] = address
            } > 0
    }

    override suspend fun deleteAddress(addressID: Int): Boolean = dbQuery {
        Addresses.deleteWhere { Addresses.addressID eq addressID } > 0
    }


}


val dao = DAOFacadeImpl().apply {
    runBlocking {
        if (allAccounts().isEmpty()) {
            addNewAccount(
                username = "admin@gmail.com",
                password = "admin"
            )
        }
        if (allUsers().isEmpty()){
            addNewUser(
                "admin",
                22,
                "27/10/2002",
                true,
                "0777376750",
                "admin@gmail.com"
            )
        }
        if (allProducts().isEmpty()) {
            addNewProduct(
                "Package of genuine Delonghi coffee beans 250g",
                50.5f,
                "https://product.hstatic.net/200000661969/product/goi_hat_ca_phe_delonghi_chinh_hang_250g_5_70cab7b98fb54b138214e219ea4c5465.png",
                "Enjoy great light roast coffee with De'Longhi Caffe Crema coffee beans"
            )
            addNewProduct(
                "PREMIUM CENTRAL PLAINS ROASTED BUTTER COFFEE (NO. 4 BEANS) – 3KG BAG",
                49f,
                "https://lh4.googleusercontent.com/proxy/6oSkDaMd_yjFZuY_HDjRMz9RvMBhUNzKWwEka9iowIvaqmXLhIF_fUFtulle5aoC_fqa2WTGvJkSk7G41qNOjUDN14ml8k7QqiZLnrncTeQsNQDkCMtkDiaEr2CuNtVQZFQXRd63l7XeShyqfcMtnjWzsL1fGQ",
                "Premium Culi (No. 4 bean) has water mixed with dark cockroach wings, rich aroma and long last."
            )
            addNewProduct(
                "Premium 1st Grade Pure Coffee (Unique)",
                22.4f,
                "https://capherangxay.vn/wp-content/uploads/2018/07/1C6A6740-683x1024.jpg",
                "Premium Pure Coffee Beans 1 100% Clean Especially has a rich bitter taste, light aroma, medium caffeine, acrid, sweet aftertaste."
            )
            addNewProduct(
                "Pure Arabica Coffee With Avocado - 300gr Pack",
                12.3f,
                "https://www.rey.cafe/131-large_default/ca-phe-arabica-nguyen-chat-co-bo-goi-300gr.jpg",
                "Arabica coffee is also known as tea coffee in Vietnamese. Arabica coffee is a coffee discovered before Robusta, which has a longer history than Robusta"
            )
            addNewProduct(
                "Passion Moka Coffee - 500gr Pack - Pure Moka-Robusta-Loris Nut Ingredients With Butter",
                6.6f,
                "https://www.rey.cafe/51-large_default/ca-phe-moka-dam-me-goi-500gr-thanh-phan-hat-moka-robusta-culi-nguyen-chat-co-bo.jpg",
                "Moka Passion is the perfect combination of the ecstatic aroma of Moka coffee, the rich bitterness of loris coffee and the caffeine richness of Robusta coffee. Moka Passion is an excellent coffee line, both powerful and exuding aroma, sour and bitter taste harmoniously blended."
            )
            addNewProduct(
                "Premium coffee in Nui Street",
                7f,
                "https://caphephonui.com/wp-content/uploads/1-5-768x763.jpg",
                "Coffee is an indispensable beverage in our daily life. With many different types of coffee, premium coffee in the mountain town becomes a favorite choice of coffee lovers and enthusiasts who experience traditional coffee taste."
            )
            addNewProduct(
                "Dak Lak series Gustoso premium pure bean coffee - Specialized for coffee machines",
                10.4f,
                "https://product.hstatic.net/1000360575/product/k-chuyen-dung-cho-may-pha-ca-phe-gia-dinh-tu-dong-ban-tu-dong-delonghi_f703be97ed9f4400ac1a531859128cf5_master.png",
                "Try Gustoso Dak Lak Fine Coffee – 100% Fine Robusta – for an authentic coffee taste"
            )
            addNewProduct(
                "1st Grade Pure Coffee (Passion)",
                8.5f,
                "https://caphenguyenchat.vn/wp-content/uploads/2018/07/1C6A6763-640x960.jpg",
                "1 Fine Nut Pure Coffee 100% Clean Especially has a rich bitter taste, light aroma, medium caffeine, acrid, sweet aftertaste."
            )
            addNewProduct(
                "Drip Coffee 4 – Premium Lori",
                43f,
                "https://trungnguyenlegendcafe.net/wp-content/uploads/2021/08/Tem-Hat-4-Culi-Thuong-Hang-3kg-768x763.jpg",
                "A great coffee for coffee connoisseurs, no matter how busy, no matter how far the shop is, they are ready to \"take effort, lose strength\" to come and enjoy the typical coffee flavor. Coffee here is not just a drink but the enjoyment and experience of the best coffee beans in the world."
            )
        }
    }
}