package com.example.shoppingcart.ui.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shoppingcart.R
import com.example.shoppingcart.ShopperSession
import com.example.shoppingcart.navigation.LoginScreen

@Composable
fun ProfileScreen(navController: NavController, session: ShopperSession) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {

            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .padding(4.dp)
            )
            Text(
                text = session.getUser()!!.name, style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 4.dp),
                color = Color.Black
            )
            Text(
                text = session.getUser()!!.email, style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.size(24.dp))
            ProfileContent(
                painter = painterResource(id = R.drawable.baseline_person_24),
                title = "Profile"
            )
            Spacer(modifier = Modifier.size(16.dp))
            ProfileContent(
                painter = painterResource(id = R.drawable.ic_settings),
                title = "Setting"
            )
            Spacer(modifier = Modifier.size(16.dp))
            ProfileContent(
                painter = painterResource(id = R.drawable.ic_email),
                title = "Contact"
            )
            Spacer(modifier = Modifier.size(16.dp))
            ProfileContent(
                painter = painterResource(id = R.drawable.ic_share),
                title = "Share App"
            )
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = "LogOut", style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable {
                        session.logout()
                        navController.navigate(LoginScreen)
                    },
                color = Color.Blue
            )
        }
    }
}

@Composable
fun ProfileContent(painter: Painter, title: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(alpha = 0.4f))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painter,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_forward),
                contentDescription = ""
            )
        }
    }
}
