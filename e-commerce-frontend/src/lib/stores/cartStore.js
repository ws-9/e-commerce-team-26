import { writable } from 'svelte/store';

function createCart() {
	const { subscribe, set, update } = writable([]);

	return {
		subscribe,

		add(product) {
			update(items => {
				const existing = items.find(item => item.id === product.id);

				if (existing) {
					return items.map(item =>
						item.id === product.id
							? { ...item, quantity: item.quantity + 1 }
							: item
					);
				}

				return [...items, { ...product, quantity: 1 }];
			});
		},

		remove(productId) {
			update(items => items.filter(item => item.id !== productId));
		},

		clear() {
			set([]);
		}
	};
}

export const cartStore = createCart();
