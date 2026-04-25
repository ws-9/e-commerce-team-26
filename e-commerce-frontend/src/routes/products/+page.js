import { catalogService } from '$lib/services/catalogService.js';

function getFilters(url) {
	return {
		query: url.searchParams.get('query') ?? '',
		category: url.searchParams.get('category') ?? '',
		minPrice: url.searchParams.get('minPrice') ?? '',
		maxPrice: url.searchParams.get('maxPrice') ?? '',
		sort: url.searchParams.get('sort') ?? 'featured',
		page: Number(url.searchParams.get('page') ?? 1)
	};
}

export async function load({ fetch, url }) {
	const filters = getFilters(url);

	try {
		const products = await catalogService.getProducts({ fetch });
		const result = catalogService.searchProducts(products, filters);

		return {
			filters,
			products: result.items,
			totalProducts: result.total,
			pagination: result.pagination,
			categories: catalogService.getCategories(products),
			featuredProducts: catalogService.getFeaturedProducts(products)
		};
	} catch (error) {
		return {
			filters,
			products: [],
			totalProducts: 0,
			pagination: {
				page: 1,
				pageSize: 8,
				totalPages: 1
			},
			categories: [],
			featuredProducts: [],
			error: error.message
		};
	}
}
