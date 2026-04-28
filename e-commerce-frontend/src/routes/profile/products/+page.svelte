<script>
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';
	import { formatCurrency } from '$lib/utils/formatters.js';
	import { authStore } from '$lib/stores/authStore.js';
	import { catalogService } from '$lib/services/catalogService.js';

	let products = $state([]);
	let error = $state('');
	let loading = $state(true);
	let isCreating = $state(false);
	let editingId = $state(null);

	// Form fields for create/edit
	let name = $state('');
	let description = $state('');
	let price = $state(0);
	let stockQty = $state(0);
	let formLoading = $state(false);

	async function loadMyProducts() {
		try {
			const res = await fetch('http://localhost:8080/api/me/products', {
				headers: {
					Authorization: 'Basic ' + btoa(`${$authStore.email}:${$authStore.password}`)
				}
			});

			if (!res.ok) {
				error = 'Failed to load your products.';
				return;
			}

			products = await res.json();
		} catch {
			error = 'Error connecting to the product catalog.';
		} finally {
			loading = false;
		}
	}

	function startCreate() {
		name = '';
		description = '';
		price = 0;
		stockQty = 0;
		isCreating = true;
		editingId = null;
	}

	function startEdit(product) {
		name = product.name;
		description = product.description;
		price = product.price;
		stockQty = product.stockQty;
		editingId = product.id;
		isCreating = false;
	}

	async function handleCreate(e) {
		e.preventDefault();
		formLoading = true;
		try {
			await catalogService.createProduct({
				name,
				description,
				price,
				stockQty
			}, { fetch });
			
			isCreating = false;
			await loadMyProducts();
		} catch (err) {
			alert(err.message);
		} finally {
			formLoading = false;
		}
	}

	async function handleUpdate(e) {
		e.preventDefault();
		formLoading = true;
		try {
			await catalogService.updateProduct(editingId, {
				name,
				description,
				price,
				stockQty
			}, { fetch });
			
			editingId = null;
			await loadMyProducts();
		} catch (err) {
			alert(err.message);
		} finally {
			formLoading = false;
		}
	}

	async function handleDelete(id) {
		if (!confirm('Are you sure you want to delete this product?')) return;
		
		try {
			await catalogService.deleteProduct(id, { fetch });
			await loadMyProducts();
		} catch (err) {
			alert(err.message);
		}
	}

	loadMyProducts();
</script>

<svelte:head>
	<title>My Inventory</title>
</svelte:head>

<StorefrontShell>
	<div class="space-y-8">
		<header class="flex items-end justify-between">
			<div>
				<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">Seller Hub</p>
				<h1 class="mt-2 text-4xl font-black tracking-tight text-slate-950">My Products</h1>
			</div>
			{#if !isCreating && !editingId}
				<button 
					class="rounded-xl bg-blue-600 px-6 py-3 text-sm font-bold text-white transition hover:bg-blue-700 shadow-sm"
					onclick={startCreate}
				>
					Add New Product
				</button>
			{/if}
		</header>

		{#if isCreating || editingId}
			<div class="rounded-[2rem] border border-slate-200 bg-white p-8 shadow-sm">
				<form onsubmit={isCreating ? handleCreate : handleUpdate} class="space-y-6">
					<div>
						<h2 class="text-2xl font-black text-slate-950">{isCreating ? 'List New Product' : 'Edit Product'}</h2>
						<p class="text-sm text-slate-500">{isCreating ? 'Provide details for your new marketplace listing.' : 'Update your product information and stock levels.'}</p>
					</div>

					<div class="grid gap-6 sm:grid-cols-2">
						<div>
							<label for="p-name" class="mb-2 block text-sm font-bold text-slate-700">Product Name</label>
							<input
								id="p-name"
								type="text"
								bind:value={name}
								required
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							/>
						</div>
						<div>
							<label for="p-price" class="mb-2 block text-sm font-bold text-slate-700">Price ($)</label>
							<input
								id="p-price"
								type="number"
								step="0.01"
								min="0"
								bind:value={price}
								required
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							/>
						</div>
					</div>

					<div class="grid gap-6 sm:grid-cols-2">
						<div>
							<label for="p-stock" class="mb-2 block text-sm font-bold text-slate-700">Stock Quantity</label>
							<input
								id="p-stock"
								type="number"
								min="0"
								bind:value={stockQty}
								required
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							/>
						</div>
						<div class="flex flex-col justify-end">
							<p class="text-xs text-slate-500 italic">Category: Default Category (fixed)</p>
						</div>
					</div>

					<div>
						<label for="p-desc" class="mb-2 block text-sm font-bold text-slate-700">Description</label>
						<textarea
							id="p-desc"
							bind:value={description}
							required
							rows="3"
							class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
						></textarea>
					</div>

					<div class="flex gap-3">
						<button
							type="submit"
							disabled={formLoading}
							class="cta-button flex-grow"
						>
							{formLoading ? 'Saving…' : (isCreating ? 'Create Product' : 'Save Changes')}
						</button>
						<button
							type="button"
							onclick={() => { isCreating = false; editingId = null; }}
							class="rounded-2xl border border-slate-200 bg-white px-6 py-3 text-sm font-bold text-slate-700 transition hover:bg-slate-50"
						>
							Cancel
						</button>
					</div>
				</form>
			</div>
		{/if}

		{#if loading}
			<div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
				{#each Array(3) as _}
					<div class="h-64 animate-pulse rounded-[2rem] bg-slate-200"></div>
				{/each}
			</div>
		{:else if error}
			<div class="rounded-2xl border border-red-200 bg-red-50 p-6 text-red-700">
				<p class="font-bold">{error}</p>
			</div>
		{:else if products.length === 0}
			<div class="rounded-[2rem] border border-slate-200 bg-white p-12 text-center shadow-sm">
				<div class="mx-auto flex h-20 w-20 items-center justify-center rounded-full bg-slate-100 text-slate-400">
					<svg class="h-10 w-10" fill="none" viewBox="0 0 24 24" stroke="currentColor">
						<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
					</svg>
				</div>
				<h2 class="mt-6 text-2xl font-bold text-slate-950">No products listed yet.</h2>
				<p class="mt-2 text-slate-600">Start adding items to your inventory to begin selling.</p>
				<button class="cta-button mt-8 inline-flex" onclick={startCreate}>List a product</button>
			</div>
		{:else}
			<div class="overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-sm">
				<table class="w-full text-left">
					<thead class="bg-slate-50 border-b border-slate-200">
						<tr>
							<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Product</th>
							<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Category</th>
							<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Price</th>
							<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Stock</th>
							<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase text-right">Actions</th>
						</tr>
					</thead>
					<tbody class="divide-y divide-slate-100">
						{#each products as product (product.id)}
							<tr class="transition hover:bg-slate-50/50" class:bg-blue-50={editingId === product.id}>
								<td class="px-8 py-6">
									<div class="flex items-center gap-4">
										<div class="flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-xl bg-slate-100 text-lg font-bold text-slate-300">
											{product.name[0]}
										</div>
										<div>
											<p class="font-bold text-slate-950">{product.name}</p>
											<p class="text-xs text-slate-500">ID: {product.id}</p>
										</div>
									</div>
								</td>
								<td class="px-8 py-6">
									<span class="inline-flex rounded-full bg-slate-100 px-3 py-1 text-xs font-bold text-slate-600">
										{product.categoryName}
									</span>
								</td>
								<td class="px-8 py-6">
									<p class="font-bold text-slate-950">{formatCurrency(product.price)}</p>
								</td>
								<td class="px-8 py-6">
									<p class:text-red-600={product.stockQty <= 5} class="text-sm font-bold">
										{product.stockQty} units
									</p>
								</td>
								<td class="px-8 py-6 text-right">
									<div class="flex justify-end gap-4">
										<button 
											onclick={() => startEdit(product)}
											class="text-sm font-bold text-blue-600 hover:text-blue-700"
										>
											Edit
										</button>
										<button 
											onclick={() => handleDelete(product.id)}
											class="text-sm font-bold text-red-600 hover:text-red-700"
										>
											Delete
										</button>
									</div>
								</td>
							</tr>
						{/each}
					</tbody>
				</table>
			</div>
		{/if}
	</div>
</StorefrontShell>
