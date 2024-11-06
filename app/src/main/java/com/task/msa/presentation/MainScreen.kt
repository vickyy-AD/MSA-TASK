package com.task.msa.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.task.msa.R
import com.task.msa.data.PizzaJuiceRepository
import com.task.msa.data.Place

@Composable
fun MainScreenUi(innerPadding: PaddingValues) {
    val viewModel: PizzaJuiceViewModel =
        viewModel(factory = ViewModelFactory(PizzaJuiceRepository()))

    LaunchedEffect(Unit) {
        viewModel.loadPizzaPlaces()
        viewModel.loadJuicePlaces()
    }

    val pizzaPlaces by viewModel.pizzaPlaces.collectAsState()
    val juicePlaces by viewModel.juicePlaces.collectAsState()


    val tabTitles = listOf("Pizza", "Juice")


    var selectedTabIndex by remember { mutableIntStateOf(0) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "Nearby Pizza & Juice Shops",
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                modifier = Modifier
                    .padding(16.dp)

            )
        }
        HorizontalDivider(thickness = 0.3.dp)

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions ->

                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color.Black
                )
            }
        ) {

            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.Gray,
                    text = {
                        Text(
                            if (selectedTabIndex == index) title.uppercase() else title,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                )
            }
        }


        when (selectedTabIndex) {
            0 -> ResponseHandling(pizzaPlaces)
            1 -> ResponseHandling(juicePlaces)
        }

    }
}

@Composable
fun ResponseHandling(pizzaPlaces: ResponseHandler<List<Place>>) {
    when (pizzaPlaces) {
        is ResponseHandler.Loading -> {
            Spacer(modifier = Modifier.height(40.dp))
            CircularProgressIndicator(color = Color.Black)
        }

        is ResponseHandler.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Error loading data. Please try again.",
                    color = Color.Red,
                )
            }
        }

        is ResponseHandler.Success -> {
            LazyColumn {
                itemsIndexed(pizzaPlaces.data) { index, place ->
                    PlaceItem(place, index % 2 == 0)
                    HorizontalDivider(thickness = 0.2.dp, color = Color.Gray)
                }

            }
        }
    }
}

@Composable
fun PlaceItem(place: Place, isEven: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isEven) colorResource(id = R.color.orange) else Color.White
            )
            .padding(16.dp)
    ) {
        Text(text = place.name, fontWeight = FontWeight.Normal, fontFamily = FontFamily.SansSerif)

    }
}
