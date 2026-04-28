import { writable } from 'svelte/store';
import { browser } from '$app/environment';

function getStoredCart() {
	if (!browser) return [];
	try {
		const raw = localStorage.getItem('cart');
		return raw ? JSON.parse(raw) : [];
	} catch {
		return [];
	}
}

function persistCart(items) {
	if (browser) localStorage.setItem('cart', JSON.stringify(items));
}

function createCart() {
	const initial = getStoredCart();
	const { subscribe, set, update } = writable(initial);

	return {
		subscribe,

		add(product) {
			update(items => {
				const existing = items.find(item => item.id === product.id);
				let newItems;

				if (existing) {
					newItems = items.map(item =>
						item.id === product.id
							? { ...item, quantity: item.quantity + 1 }
							: item
					);
				} else {
					newItems = [...items, { ...product, quantity: 1 }];
				}

				persistCart(newItems);
				return newItems;
			});
		},

		remove(productId) {
			update(items => {
				const newItems = items.filter(item => item.id !== productId);
				persistCart(newItems);
				return newItems;
			});
		},

		clear() {
			set([]);
			persistCart([]);
		}
	};
}

export const cartStore = createCart();
