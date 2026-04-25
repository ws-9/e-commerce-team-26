<script>
	import ProductCard from '$lib/components/ProductCard.svelte';
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';
	import { formatCurrency } from '$lib/utils/formatters.js';

	let { data } = $props();

	const sortOptions = [
		{ value: 'featured', label: 'Featured' },
		{ value: 'price-asc', label: 'Price: Low to high' },
		{ value: 'price-desc', label: 'Price: High to low' },
		{ value: 'rating-desc', label: 'Top rated' }
	];

	function buildPageHref(page) {
		const params = new URLSearchParams();

		if (data.filters.query) params.set('query', data.filters.query);
		if (data.filters.category) params.set('category', data.filters.category);
		if (data.filters.minPrice) params.set('minPrice', data.filters.minPrice);
		if (data.filters.maxPrice) params.set('maxPrice', data.filters.maxPrice);
		if (data.filters.sort && data.filters.sort !== 'featured')
			params.set('sort', data.filters.sort);
		if (page > 1) params.set('page', String(page));

		const query = params.toString();
		return query ? `/products?${query}` : '/products';
	}
</script>

<svelte:head>
	<title>Browse Products</title>
</svelte:head>

<StorefrontShell categories={data.categories}>
	<div class="space-y-8">
		<section
			class="overflow-hidden rounded-[2rem] bg-[linear-gradient(135deg,#0f172a_0%,#1d4ed8_45%,#38bdf8_100%)] px-6 py-10 text-white shadow-xl lg:px-10"
		>
			<p class="text-sm font-semibold tracking-[0.35em] text-blue-100 uppercase">Marketplace</p>
			<h1 class="mt-3 max-w-3xl text-4xl font-black tracking-tight sm:text-5xl">
				Shop products from verified Team 26 sellers.
			</h1>
			<p class="mt-4 max-w-2xl text-base leading-7 text-blue-50/90">
				Browse live inventory, compare categories, and add products to your cart without leaving the
				shopper experience.
			</p>
		</section>

		{#if data.error}
			<section class="rounded-[2rem] border border-red-200 bg-red-50 p-8 text-red-700 shadow-sm">
				<h2 class="text-xl font-bold">We couldn’t load the product catalog.</h2>
				<p class="mt-2 text-sm">{data.error}</p>
			</section>
		{:else}
			<section class="grid gap-6 lg:grid-cols-[280px,1fr]">
				<aside class="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm">
					<h2 class="text-lg font-bold text-slate-950">Refine results</h2>
					<form action="/products" class="mt-5 space-y-5">
						<div>
							<label class="mb-2 block text-sm font-semibold text-slate-700" for="query"
								>Search</label
							>
							<input
								id="query"
								name="query"
								type="search"
								value={data.filters.query}
								placeholder="Search by product or seller"
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 ring-0 transition outline-none focus:border-sky-500"
							/>
						</div>

						<div>
							<label class="mb-2 block text-sm font-semibold text-slate-700" for="category">
								Category
							</label>
							<select
								id="category"
								name="category"
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							>
								<option value="">All categories</option>
								{#each data.categories as category}
									<option value={category.name} selected={data.filters.category === category.name}>
										{category.name}
									</option>
								{/each}
							</select>
						</div>

						<div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-1">
							<div>
								<label class="mb-2 block text-sm font-semibold text-slate-700" for="minPrice">
									Min price
								</label>
								<input
									id="minPrice"
									name="minPrice"
									type="number"
									min="0"
									step="1"
									value={data.filters.minPrice}
									placeholder="0"
									class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
								/>
							</div>
							<div>
								<label class="mb-2 block text-sm font-semibold text-slate-700" for="maxPrice">
									Max price
								</label>
								<input
									id="maxPrice"
									name="maxPrice"
									type="number"
									min="0"
									step="1"
									value={data.filters.maxPrice}
									placeholder="999"
									class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
								/>
							</div>
						</div>

						<div>
							<label class="mb-2 block text-sm font-semibold text-slate-700" for="sort">Sort</label>
							<select
								id="sort"
								name="sort"
								class="w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm text-slate-950 transition outline-none focus:border-sky-500"
							>
								{#each sortOptions as option}
									<option value={option.value} selected={data.filters.sort === option.value}>
										{option.label}
									</option>
								{/each}
							</select>
						</div>

						<button class="cta-button w-full" type="submit">Apply filters</button>
					</form>
				</aside>

				<div class="space-y-6">
					<div
						class="flex flex-col gap-4 rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm"
					>
						<div class="flex flex-col gap-4 md:flex-row md:items-end md:justify-between">
							<div>
								<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">
									Browse
								</p>
								<h2 class="mt-2 text-2xl font-bold text-slate-950">
									{data.totalProducts}
									{data.totalProducts === 1 ? 'product' : 'products'}
								</h2>
								<p class="mt-2 text-sm text-slate-600">
									Page {data.pagination.page} of {data.pagination.totalPages}
								</p>
							</div>
							<div class="flex flex-wrap gap-2">
								{#each data.categories as category}
									<a
										class:text-white={data.filters.category === category.name}
										class:bg-slate-950={data.filters.category === category.name}
										class:border-slate-950={data.filters.category === category.name}
										class="rounded-full border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:border-slate-950 hover:text-slate-950"
										href={`/products?category=${encodeURIComponent(category.name)}`}
									>
										{category.name}
									</a>
								{/each}
							</div>
						</div>

						{#if !data.totalProducts}
							<div class="rounded-[1.5rem] bg-slate-50 p-8 text-center">
								<h3 class="text-xl font-bold text-slate-950">No products match those filters.</h3>
								<p class="mt-2 text-sm text-slate-600">
									Try clearing a category or widening your price range.
								</p>
								<a class="cta-button mt-5 inline-flex" href="/products">Reset filters</a>
							</div>
						{:else}
							<div class="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
								{#each data.products as product}
									<ProductCard {product} />
								{/each}
							</div>
						{/if}
					</div>

					{#if data.totalProducts > 0}
						<nav class="flex flex-wrap items-center justify-center gap-3">
							{#each Array(data.pagination.totalPages) as _, index}
								{@const page = index + 1}
								<a
									class:bg-slate-950={page === data.pagination.page}
									class:text-white={page === data.pagination.page}
									class:border-slate-950={page === data.pagination.page}
									class="rounded-full border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:border-slate-950 hover:text-slate-950"
									href={buildPageHref(page)}
								>
									{page}
								</a>
							{/each}
						</nav>
					{/if}
				</div>
			</section>

			{#if data.featuredProducts.length}
				<section class="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm">
					<div class="flex flex-col gap-2 sm:flex-row sm:items-end sm:justify-between">
						<div>
							<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">
								Featured now
							</p>
							<h2 class="mt-2 text-2xl font-bold text-slate-950">
								Popular picks across the marketplace
							</h2>
						</div>
						<p class="text-sm text-slate-600">
							Strong value from {formatCurrency(
								Math.min(...data.featuredProducts.map((product) => product.price))
							)} and up.
						</p>
					</div>
					<div class="mt-6 grid gap-5 md:grid-cols-2 xl:grid-cols-3">
						{#each data.featuredProducts as product}
							<ProductCard {product} />
						{/each}
					</div>
				</section>
			{/if}
		{/if}
	</div>
</StorefrontShell>
