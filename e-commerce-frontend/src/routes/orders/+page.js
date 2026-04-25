import { catalogService } from '$lib/services/catalogService.js';
import { orderService } from '$lib/services/orderService.js';

export async function load({ fetch }) {
	try {
		const [orders, products] = await Promise.all([
			orderService.getOrders({ fetch }),
			catalogService.getProducts({ fetch })
		]);

		return {
			orders,
			categories: catalogService.getCategories(products)
		};
	} catch (error) {
		return {
			orders: [],
			categories: [],
			error: error.message
		};
	}
}
