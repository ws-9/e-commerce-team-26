<script>
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';
	import { authStore } from '$lib/stores/authStore.js';
	import { getAuthHeader } from '$lib/utils/basicAuth.js';
	import { formatCurrency } from '$lib/utils/formatters.js';

	let users = $state([]);
	let sellers = $state([]);
	let products = $state([]);
	let error = $state('');
	let loading = $state(true);
	let activeTab = $state('users');

	async function fetchData() {
		const auth = $authStore;
		const header = getAuthHeader(auth.email, auth.password);
		
		try {
			const [usersRes, sellersRes, productsRes] = await Promise.all([
				fetch('http://localhost:8080/api/users', { headers: { Authorization: header } }),
				fetch('http://localhost:8080/api/sellers', { headers: { Authorization: header } }),
				fetch('http://localhost:8080/api/products', { headers: { Authorization: header } })
			]);

			if (!usersRes.ok || !sellersRes.ok || !productsRes.ok) {
				error = 'Failed to fetch admin data. Requires admin privileges.';
				return;
			}

			users = await usersRes.json();
			sellers = await sellersRes.json();
			products = await productsRes.json();
		} catch (err) {
			error = 'Connection error.';
		} finally {
			loading = false;
		}
	}

	async function deleteUser(id) {
		if (!confirm('Are you sure you want to delete this user? This action is permanent.')) return;
		const auth = $authStore;
		try {
			const res = await fetch(`http://localhost:8080/api/users/${id}`, {
				method: 'DELETE',
				headers: { Authorization: getAuthHeader(auth.email, auth.password) }
			});
			if (!res.ok) throw new Error('Deletion failed');
			users = users.filter(u => u.id !== id);
		} catch (err) {
			alert(err.message);
		}
	}

	async function deleteSeller(id) {
		if (!confirm('Are you sure you want to delete this seller? This action is permanent.')) return;
		const auth = $authStore;
		try {
			const res = await fetch(`http://localhost:8080/api/sellers/${id}`, {
				method: 'DELETE',
				headers: { Authorization: getAuthHeader(auth.email, auth.password) }
			});
			if (!res.ok) throw new Error('Deletion failed');
			sellers = sellers.filter(s => s.userId !== id);
		} catch (err) {
			alert(err.message);
		}
	}

	async function deleteProduct(id) {
		if (!confirm('Are you sure you want to delete this product? This action cannot be undone.')) return;
		const auth = $authStore;
		try {
			const res = await fetch(`http://localhost:8080/api/products/${id}`, {
				method: 'DELETE',
				headers: { Authorization: getAuthHeader(auth.email, auth.password) }
			});
			if (!res.ok) throw new Error('Product deletion failed');
			products = products.filter(p => p.id !== id);
		} catch (err) {
			alert(err.message);
		}
	}

	fetchData();
</script>

<svelte:head>
	<title>Admin Dashboard</title>
</svelte:head>

<StorefrontShell>
	<div class="space-y-8">
		<header>
			<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">Management</p>
			<h1 class="mt-2 text-4xl font-black tracking-tight text-slate-950">Admin Tasks</h1>
		</header>

		{#if loading}
			<div class="h-64 animate-pulse rounded-[2rem] bg-slate-200"></div>
		{:else if error}
			<div class="rounded-2xl border border-red-200 bg-red-50 p-6 text-red-700">
				<p class="font-bold">{error}</p>
			</div>
		{:else}
			<div class="flex gap-4 border-b border-slate-200 pb-1 overflow-x-auto">
				<button 
					onclick={() => activeTab = 'users'}
					class="px-4 py-2 font-bold text-sm transition whitespace-nowrap"
					class:text-slate-950={activeTab === 'users'}
					class:border-b-2={activeTab === 'users'}
					class:border-slate-950={activeTab === 'users'}
					class:text-slate-400={activeTab !== 'users'}
				>
					Users ({users.length})
				</button>
				<button 
					onclick={() => activeTab = 'sellers'}
					class="px-4 py-2 font-bold text-sm transition whitespace-nowrap"
					class:text-slate-950={activeTab === 'sellers'}
					class:border-b-2={activeTab === 'sellers'}
					class:border-slate-950={activeTab === 'sellers'}
					class:text-slate-400={activeTab !== 'sellers'}
				>
					Sellers ({sellers.length})
				</button>
				<button 
					onclick={() => activeTab = 'products'}
					class="px-4 py-2 font-bold text-sm transition whitespace-nowrap"
					class:text-slate-950={activeTab === 'products'}
					class:border-b-2={activeTab === 'products'}
					class:border-slate-950={activeTab === 'products'}
					class:text-slate-400={activeTab !== 'products'}
				>
					Products ({products.length})
				</button>
			</div>

			<div class="overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-sm">
				{#if activeTab === 'users'}
					<table class="w-full text-left">
						<thead class="bg-slate-50 border-b border-slate-200">
							<tr>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">ID</th>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Name</th>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Email</th>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase text-right">Actions</th>
							</tr>
						</thead>
						<tbody class="divide-y divide-slate-100">
							{#each users as user (user.id)}
								<tr class="transition hover:bg-slate-50/50">
									<td class="px-8 py-6 font-mono text-sm text-slate-500">#{user.id}</td>
									<td class="px-8 py-6 font-bold text-slate-950">{user.name}</td>
									<td class="px-8 py-6 text-slate-600">{user.email}</td>
									<td class="px-8 py-6 text-right">
										<button 
											onclick={() => deleteUser(user.id)}
											class="text-sm font-bold text-red-600 hover:text-red-700"
										>
											Delete
										</button>
									</td>
								</tr>
							{/each}
						</tbody>
					</table>
				{:else if activeTab === 'sellers'}
					<table class="w-full text-left">
						<thead class="bg-slate-50 border-b border-slate-200">
							<tr>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Business</th>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Contact</th>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Status</th>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase text-right">Actions</th>
							</tr>
						</thead>
						<tbody class="divide-y divide-slate-100">
							{#each sellers as seller (seller.userId)}
								<tr class="transition hover:bg-slate-50/50">
									<td class="px-8 py-6">
										<p class="font-bold text-slate-950">{seller.businessName}</p>
										<p class="text-xs text-slate-400 italic">User ID: {seller.userId}</p>
									</td>
									<td class="px-8 py-6 text-slate-600">{seller.contactEmail}</td>
									<td class="px-8 py-6">
										<span class="inline-flex rounded-full px-3 py-1 text-[10px] font-black tracking-wider uppercase"
											class:bg-emerald-100={seller.verified} class:text-emerald-700={seller.verified}
											class:bg-amber-100={!seller.verified} class:text-amber-700={!seller.verified}>
											{seller.verified ? 'Verified' : 'Pending'}
										</span>
									</td>
									<td class="px-8 py-6 text-right">
										<button 
											onclick={() => deleteSeller(seller.userId)}
											class="text-sm font-bold text-red-600 hover:text-red-700"
										>
											Delete
										</button>
									</td>
								</tr>
							{/each}
						</tbody>
					</table>
				{:else if activeTab === 'products'}
					<table class="w-full text-left">
						<thead class="bg-slate-50 border-b border-slate-200">
							<tr>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Product</th>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Seller</th>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase">Price</th>
								<th class="px-8 py-4 text-xs font-bold tracking-wider text-slate-400 uppercase text-right">Actions</th>
							</tr>
						</thead>
						<tbody class="divide-y divide-slate-100">
							{#each products as product (product.id)}
								<tr class="transition hover:bg-slate-50/50">
									<td class="px-8 py-6">
										<p class="font-bold text-slate-950">{product.name}</p>
										<p class="text-xs text-slate-400 uppercase font-black tracking-widest">{product.categoryName}</p>
									</td>
									<td class="px-8 py-6">
										<p class="text-sm font-bold text-slate-700">{product.sellerName}</p>
										<p class="text-xs text-slate-400">ID: {product.sellerId}</p>
									</td>
									<td class="px-8 py-6 font-bold text-slate-950">
										{formatCurrency(product.price)}
									</td>
									<td class="px-8 py-6 text-right">
										<button 
											onclick={() => deleteProduct(product.id)}
											class="text-sm font-bold text-red-600 hover:text-red-700"
										>
											Delete
										</button>
									</td>
								</tr>
							{/each}
						</tbody>
					</table>
				{/if}
			</div>
		{/if}
	</div>
</StorefrontShell>
