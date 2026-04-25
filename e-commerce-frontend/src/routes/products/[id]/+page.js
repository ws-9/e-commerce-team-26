import { catalogService } from '$lib/services/catalogService.js';

export async function load({ fetch, params }) {
	try {
		const [product, allProducts] = await Promise.all([
			catalogService.getProductById(params.id, { fetch }),
			catalogService.getProducts({ fetch })
		]);

		return {
			product,
			relatedProducts: catalogService.getRelatedProducts(allProducts, product.id),
			categories: catalogService.getCategories(allProducts)
		};
	} catch (error) {
		return {
			product: null,
			relatedProducts: [],
			categories: [],
			error: error.message
		};
	}
}
