package com.example.salesapp.model
import kotlinx.serialization.Serializable 

@Serializable 
data class SaleItem (
    val id: Int,
    val description: String,
    val price: Int,
    val sellerEmail: String,
    val sellerPhone: String,
    val time: Long, // Ændret til Long for korrekt tidshåndtering
    val pictureUrl: String? = null
)
{
    override fun toString(): String{
        return "SaleItem(id=$id, description='$description', price=$price, sellerEmail='$sellerEmail', sellerPhone='$sellerPhone', time=$time, pictureUrl='$pictureUrl')"
    }
}
