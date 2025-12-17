sealed class NavRoutes(val route: String) {
    data object Register : NavRoutes("register")
    data object SalesItems : NavRoutes("salesItems")
    data object AddItem : NavRoutes("addItem")

    data object ItemDetails : NavRoutes("itemDetails") {
        fun createRoute(itemId: Int) = "$route/$itemId"
    }
}