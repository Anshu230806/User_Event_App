package com.example.eventsapp.feature.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventsapp.R
import com.example.eventsapp.feature.theme.EventsAppTheme


@Composable
fun onBoarding(
    modifier: Modifier = Modifier,
    onGetStartedClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {

    Box(
        modifier = modifier
    ){
        Image(
            painter = painterResource(R.drawable.starting_page),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer( modifier = Modifier.height(170.dp))

            Text(
                text = "Discover",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold

            )



            Text(
                text = "Connect",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight= FontWeight.Bold
            )


            Text(
                text = "Participate",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )


            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "All college events at\n one place",
                color = Color.White,
                fontSize = 18.sp,
                textAlign =  TextAlign.Center

            )


            Spacer(modifier = Modifier.height(320.dp))

            Button(
                modifier = Modifier.width(400.dp).height(43.dp),

                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF541A8A)),
                onClick = onGetStartedClick,
                shape = RoundedCornerShape(12.dp),
            ){
                Text(
                    text = "Get Started",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }


            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                modifier = Modifier.width(400.dp).height(43.dp),

                colors = ButtonDefaults.outlinedButtonColors( containerColor = Color.Black , contentColor =Color.White),
                onClick = onLoginClick,

                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp,Color.White)

            ){
                Text(
                    text ="Login",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }


            Spacer(modifier = Modifier.height(40.dp))
        }
    }

}



@Preview(showBackground = true)
@Composable
fun OnBoardingPreview() {
    EventsAppTheme {
        onBoarding()
    }
}