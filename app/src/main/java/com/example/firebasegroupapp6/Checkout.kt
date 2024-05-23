package com.example.firebasegroupapp6

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firebasegroupapp6.R.id.checkOutFlotingBtn
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Checkout : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        supportActionBar?.hide()
        val place_order: Button = findViewById(R.id.place_Order)

        place_order.setOnClickListener {
            AddCustomerDetails_To_Database ()
            AddOrder()
        }
        val checkOutFlotingBtn:FloatingActionButton =findViewById(checkOutFlotingBtn)
        checkOutFlotingBtn.setOnClickListener{
            startActivity(Intent(this,Cart::class.java))
            finish()
        }
    }
    fun ValidateInputs(): Boolean {
        val Customer_First_Name: EditText = findViewById(R.id.Customer_First_Name)
        val Customer_Last_Name: EditText = findViewById(R.id.Customer_Last_Name)
        val Customer_Email: EditText = findViewById(R.id.Customer_Email)
        val Customer_Phone: EditText = findViewById(R.id.Customer_Phone)
        val Customer_House_Address: EditText = findViewById(R.id.Customer_House_Address)
        val Customer_Country: EditText = findViewById(R.id.Customer_Country)
        val Customer_Postal_Code: EditText = findViewById(R.id.Customer_Postal_Code)
        val Customer_Card_Name: EditText = findViewById(R.id.Customer_Card_Name)
        val Customer_Card_Number: EditText = findViewById(R.id.Customer_Card_Number)
        val Customer_Card_Exp_Date: EditText = findViewById(R.id.Customer_Card_Exp_Date)
        val Customer_Card_Cvv: EditText = findViewById(R.id.Customer_Card_Cvv)

        val nameRegex = Regex("^[a-zA-Z]+( [a-zA-Z]+)*\$")
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        val phoneRegex = Regex("^\\d{3}-\\d{3}-\\d{4}$")
        val canadianPostalCodeRegex = Regex("^[A-Za-z]\\d[A-Za-z] \\d[A-Za-z]\\d$")
        val creditCardNumberRegex = Regex("^\\d{16}$")
        val creditCardExpDateRegex = Regex("^(0[1-9]|1[0-2])/(\\d{2})\$")
        val cvvRegex = Regex("^\\d{3}\$")

        if (Customer_First_Name.text.toString().isEmpty()) {
            Customer_First_Name.error = "First Name Required"
        } else if (!Customer_First_Name.text.toString().matches(nameRegex)) {
            Customer_First_Name.error = "Invalid First Name"
        } else if (Customer_Last_Name.text.toString().isEmpty()) {
            Customer_Last_Name.error = "Last Name Required"
        } else if (!Customer_Last_Name.text.toString().matches(nameRegex)) {
            Customer_Last_Name.error = "Invalid First Name"
        } else if (Customer_Email.text.toString().isEmpty()) {
            Customer_Email.error = "Email is required"
        } else if (!Customer_Email.text.toString().matches(emailRegex)) {
            Customer_Email.error = "Invalid Email"
        } else if (Customer_Phone.text.toString().isEmpty()) {
            Customer_Phone.error = "Phone number is required"
        } else if (!Customer_Phone.text.toString().matches(phoneRegex)) {
            Customer_Phone.error = "Invalid phone number (Use format: 000-000-0000)"
        } else if (Customer_House_Address.text.toString().isEmpty()) {
            Customer_House_Address.error = "House Address Required"
        } else if (Customer_Country.text.toString().isEmpty()) {
            Customer_Country.error = "Country Name Required"
        } else if (!Customer_Country.text.toString().matches(nameRegex)) {
            Customer_Country.error = "Invalid Country Name"
        } else if (Customer_Postal_Code.text.toString().isEmpty()) {
            Customer_Postal_Code.error = "Postal code is required"
        } else if (!Customer_Postal_Code.text.toString().matches(canadianPostalCodeRegex)) {
            Customer_Postal_Code.error = "Invalid postal code (Use format: A1A 1A1)"
        } else if (Customer_Card_Name.text.toString().isEmpty()) {
            Customer_Card_Name.error = "Card Name Required"
        } else if (!Customer_Card_Name.text.toString().matches(nameRegex)) {
            Customer_Card_Name.error = "Invalid Card  Name"
        } else if (Customer_Card_Number.text.toString().isEmpty()) {
            Customer_Card_Number.error = "Card number is required"
        } else if (!Customer_Card_Number.text.toString().matches(creditCardNumberRegex)) {
            Customer_Card_Number.error = "Invalid credit card number (must be 16 digits)"
        } else if (Customer_Card_Exp_Date.text.toString().isEmpty()) {
            Customer_Card_Exp_Date.error = "Expiration date is required"
        } else if (!Customer_Card_Exp_Date.text.toString().matches(creditCardExpDateRegex)) {
            Customer_Card_Exp_Date.error = "Invalid expiration date (use MM/YY format)"
        } else if (Customer_Card_Cvv.text.toString().isEmpty()) {
            Customer_Card_Cvv.error = "Expiration CVV required"
        } else if (!Customer_Card_Cvv.text.toString().matches(cvvRegex)) {
            Customer_Card_Cvv.error = "Invalid CVV"
        } else {
            return true
        }
        return false
    }
    fun AddCustomerDetails_To_Database () {

        val Customer_First_Name: EditText = findViewById(R.id.Customer_First_Name)
        val Customer_Last_Name: EditText = findViewById(R.id.Customer_Last_Name)
        val Customer_Email: EditText = findViewById(R.id.Customer_Email)
        val Customer_Phone: EditText = findViewById(R.id.Customer_Phone)
        val Customer_House_Address: EditText = findViewById(R.id.Customer_House_Address)
        val Customer_Country: EditText = findViewById(R.id.Customer_Country)
        val Customer_Postal_Code: EditText = findViewById(R.id.Customer_Postal_Code)
        val Customer_Card_Name: EditText = findViewById(R.id.Customer_Card_Name)
        val Customer_Card_Number: EditText = findViewById(R.id.Customer_Card_Number)
        val Customer_Card_Exp_Date: EditText = findViewById(R.id.Customer_Card_Exp_Date)
        val Customer_Card_Cvv: EditText = findViewById(R.id.Customer_Card_Cvv)

        val First_Name:String = Customer_First_Name.text.toString()
        Log.d("First_Name", "onCreate: $First_Name")
        val Last_Name:String = Customer_Last_Name.text.toString()
        val Email:String = Customer_Email.text.toString()
        val Phone: String = Customer_Phone.text.toString()
        val House_Address:String = Customer_House_Address.text.toString()
        val Country:String = Customer_Country.text.toString()
        val Postal_Code:String = Customer_Postal_Code.text.toString()
        val Card_Name:String = Customer_Card_Name.text.toString()
        val Card_Number: Int? = Customer_Card_Number.text.toString().toIntOrNull()
        val Card_Exp_Date:String = Customer_Card_Exp_Date.text.toString()
        val Card_Cvv:Int? = Customer_Card_Cvv.text.toString().toIntOrNull()


        val CurrentDate = LocalDate.now()
        val formattedDate = CurrentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        Log.d("Cvv", "onCreate: $Card_Cvv")
        Log.d("Card_Number", "onCreate: $Card_Number")
        val MyCustomer = CustomerDataClass(
            First_Name,
            Last_Name,
            Email,
            Phone,
            House_Address,
            Country,
            Postal_Code,
            Card_Name,
            Card_Number,
            Card_Exp_Date,
            Card_Cvv,
            formattedDate)


        if (ValidateInputs()) {
            val user = FirebaseAuth.getInstance().currentUser
            val uid : String? = user?.uid
            clrear_cart()
            FirebaseDatabase.getInstance().reference.child("Customer_Details/$uid").setValue(MyCustomer)
                .addOnCompleteListener{
                    Toast.makeText(this, "Order Place Successfully", Toast.LENGTH_LONG).show()
                    Log.d("First_Name", "onCreate: $MyCustomer")
                }
                .addOnFailureListener { e ->
                    // On failure
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

    }
private fun clrear_cart(){
    val user = FirebaseAuth.getInstance().currentUser
    val uid : String? = user?.uid
    FirebaseDatabase.getInstance().reference.child("Cart/$uid").removeValue().addOnCompleteListener{
        Log.d("Cart", "Cart Clear")
    }.addOnFailureListener{
        Log.d("Cart", "Cart  not Clear")
    }

}
    fun AddOrder(){
        if (ValidateInputs()){
            val user = FirebaseAuth.getInstance().currentUser
            val uid : String? = user?.uid
            val cartReference= FirebaseDatabase.getInstance().reference.child("Cart/$uid")
            cartReference.get().addOnCompleteListener{
                if(it.isSuccessful){
                    val dataSnapshot: DataSnapshot? = it.result
                    Log.d("AddOrder", "AddOrder:${dataSnapshot?.value} ")
                    val cartProduct = dataSnapshot?.value
                    val totalPriceValue: String? = intent.getStringExtra("totalPrice")
                    val cartProductCount = dataSnapshot?.childrenCount

                    val CurrentDate = LocalDate.now()
                    val formattedDate = CurrentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                    // Log the value

                    Log.d("TotalPrice", "Total Price: $totalPriceValue")
                    Log.d("cartProductCount", "Total Price: $cartProductCount")
                    val MyOrder = OrderDataClass(cartProduct as Map<String, CartDataClass>?,
                        uid,
                        cartProductCount,
                        formattedDate,
                        totalPriceValue)

                    FirebaseDatabase.getInstance().reference.child("Order").push().setValue(MyOrder)
                        .addOnCompleteListener{
                            Log.d("Order Added", "Order added to the database")
                            startActivity(Intent(this,Product::class.java))
                            finish()
                            Toast.makeText(baseContext, "ORDER PLACED", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { e ->
                            // On failure
                            //                      Toast.makeText(baseContext, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            Log.d("Error", "${e.message}")
                        }


                }

            }
        }

    }
    }



