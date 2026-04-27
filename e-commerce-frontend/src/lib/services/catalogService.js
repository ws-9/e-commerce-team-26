export const catalogService = {
async getProducts({ fetch }) {
	const auth = JSON.parse(localStorage.getItem('auth') ?? '{}');

	const res = await fetch('http://localhost:8080/api/products', {
		headers: {
			Authorization: 'Basic ' + btoa(`${auth.email}:${auth.password}`)
		}
	});

	if (!res.ok) throw new Error('Failed to load products');
	return await res.json();
},

	searchProducts(products, filters) {
		let items = [...products];

		if (filters.query) {
			items = items.filter(p =>
				p.name.toLowerCase().includes(filters.query.toLowerCase())
			);
		}

		if (filters.category) {
			items = items.filter(p => p.category?.name === filters.category);
		}

		const pageSize = 8;
		const total = items.length;
		const totalPages = Math.ceil(total / pageSize);
		const start = (filters.page - 1) * pageSize;

		return {
			items: items.slice(start, start + pageSize),
			total,
			pagination: {
				page: filters.page,
				pageSize,
				totalPages
			}
		};
	},

	getCategories(products) {
		const set = new Set(products.map(p => p.category?.name).filter(Boolean));
		return Array.from(set).map((name, i) => ({ id: i, name }));
	},

	getFeaturedProducts(products) {
		return products.slice(0, 6);
	}
};
