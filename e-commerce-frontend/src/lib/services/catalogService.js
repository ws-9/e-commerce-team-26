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

	async getProductById(id, { fetch }) {
		const auth = JSON.parse(localStorage.getItem('auth') ?? '{}');
		const res = await fetch(`http://localhost:8080/api/products/${id}`, {
			headers: {
				Authorization: 'Basic ' + btoa(`${auth.email}:${auth.password}`)
			}
		});
		if (!res.ok) throw new Error('Failed to load product');
		return await res.json();
	},

	getRelatedProducts(products, currentId) {
		const currentProduct = products.find(p => p.id === Number(currentId));
		if (!currentProduct) return [];
		return products
			.filter(p => p.category?.id === currentProduct.category?.id && p.id !== currentProduct.id)
			.slice(0, 4);
	},

	getFeaturedProducts(products) {
		return products.slice(0, 6);
	}
};
