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
		const q = filters.query.toLowerCase();

		items = items.filter(p =>
			p.name?.toLowerCase().includes(q) ||
			p.description?.toLowerCase().includes(q) ||
			p.sellerName?.toLowerCase().includes(q)
		);
	}

	if (filters.category) {
		items = items.filter(p => p.category?.name === filters.category);
	}

	if (filters.minPrice) {
		items = items.filter(p => Number(p.price) >= Number(filters.minPrice));
	}

	if (filters.maxPrice) {
		items = items.filter(p => Number(p.price) <= Number(filters.maxPrice));
	}

	if (filters.sort === 'price-asc') {
		items.sort((a, b) => Number(a.price) - Number(b.price));
	}

	if (filters.sort === 'price-desc') {
		items.sort((a, b) => Number(b.price) - Number(a.price));
	}

	if (filters.sort === 'rating-desc') {
		// no rating field in backend yet, so fallback to highest price for now
		items.sort((a, b) => Number(b.price) - Number(a.price));
	}

	const pageSize = 8;
	const total = items.length;
	const totalPages = Math.max(Math.ceil(total / pageSize), 1);
	const page = Math.min(filters.page, totalPages);
	const start = (page - 1) * pageSize;

	return {
		items: items.slice(start, start + pageSize),
		total,
		pagination: {
			page,
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
	},

	async createProduct(productData, { fetch }) {
		const auth = JSON.parse(localStorage.getItem('auth') ?? '{}');
		const res = await fetch('http://localhost:8080/api/products', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				Authorization: 'Basic ' + btoa(`${auth.email}:${auth.password}`)
			},
			body: JSON.stringify(productData)
		});
		if (!res.ok) throw new Error('Failed to create product');
		return await res.json();
	},

	async deleteProduct(id, { fetch }) {
		const auth = JSON.parse(localStorage.getItem('auth') ?? '{}');
		const res = await fetch(`http://localhost:8080/api/products/${id}`, {
			method: 'DELETE',
			headers: {
				Authorization: 'Basic ' + btoa(`${auth.email}:${auth.password}`)
			}
		});
		if (!res.ok) throw new Error('Failed to delete product');
	},

	async updateProduct(id, productData, { fetch }) {
		const auth = JSON.parse(localStorage.getItem('auth') ?? '{}');
		const res = await fetch(`http://localhost:8080/api/products/${id}`, {
			method: 'PATCH',
			headers: {
				'Content-Type': 'application/json',
				Authorization: 'Basic ' + btoa(`${auth.email}:${auth.password}`)
			},
			body: JSON.stringify(productData)
		});
		if (!res.ok) throw new Error('Failed to update product');
		return await res.json();
	}
};
