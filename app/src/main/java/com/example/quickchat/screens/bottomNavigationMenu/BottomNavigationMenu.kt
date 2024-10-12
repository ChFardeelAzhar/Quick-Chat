package com.example.quickchat.screens.bottomNavigationMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quickchat.R
import com.example.quickchat.util.NavRouts
import com.example.quickchat.util.navigateTo



@Composable
fun BottomNavigationMenu(
    navController: NavController,
    selectedRoute : MutableState<NavRouts.Destination>
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    navController.navigate( NavRouts.Destination.ChatList.route){
                        popUpTo(0)
                    }
                    selectedRoute.value = NavRouts.Destination.ChatList

                }
        ) {

            Image(
                imageVector = Icons.Outlined.Home,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                colorFilter = ColorFilter.tint(
                    color = if (selectedRoute.value == NavRouts.Destination.ChatList) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.outlineVariant
                )
            )
            Text(
                text = "Home",
                fontWeight = FontWeight.Bold,
                color = if (selectedRoute.value == NavRouts.Destination.ChatList) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.outlineVariant

            )
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    navigateTo(navController, NavRouts.Destination.StatusList.route)
                    selectedRoute.value = NavRouts.Destination.StatusList

                }
        ) {
            Image(
                painterResource(id = R.drawable.forward_circle),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                colorFilter = ColorFilter.tint(
                    color = if (selectedRoute.value == NavRouts.Destination.StatusList) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.outlineVariant
                )
            )
            Text(
                text = "Status",
                fontWeight = FontWeight.Bold,
                color = if (selectedRoute.value == NavRouts.Destination.StatusList) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.outlineVariant

            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .clickable {
                    navigateTo(navController, NavRouts.Destination.Profile.route)
                    selectedRoute.value = NavRouts.Destination.Profile
                }
        ) {
            Image(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp),
                colorFilter = ColorFilter.tint(
                    color = if (selectedRoute.value == NavRouts.Destination.Profile) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.outlineVariant
                )
            )
            Text(
                text = "Profile",
                fontWeight = FontWeight.Bold,
                color = if (selectedRoute.value == NavRouts.Destination.Profile) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.outlineVariant

            )
        }

    }


}