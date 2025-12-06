<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { Icon } from '@iconify/vue';

const products = ref([]);
const loading = ref(true);
const error = ref(null);
const currentSlide = ref(0);
const itemsPerPage = 4;

const API_URL = 'http://localhost:8080/api/san-pham'; 

const totalSlides = computed(() => Math.ceil(products.value.length / itemsPerPage));

const visibleProducts = computed(() => {
  const start = currentSlide.value * itemsPerPage;
  return products.value.slice(start, start + itemsPerPage);
});

const nextSlide = () => {
  if (currentSlide.value < totalSlides.value - 1) {
    currentSlide.value++;
  } else {
    currentSlide.value = 0;
  }
};

const prevSlide = () => {
  if (currentSlide.value > 0) {
    currentSlide.value--;
  } else {
    currentSlide.value = totalSlides.value - 1;
  }
};

const fetchProducts = async () => {
  try {
    loading.value = true;
    const response = await axios.get(API_URL);

    products.value = response.data.map(item => ({
        id: item.id,
        name: item.tenSanPham || `Sản phẩm #${item.maSanPham}`, 
        
        price: item.giaBan || 0, 
        
        image: "https://placehold.co/300x300/png?text=Giay+Dep", // Entity chưa có ảnh, dùng ảnh mẫu
        discount: 0,
        rating: 5,
        trangThai: item.trangThai
    }));

    loading.value = false;
  } catch (err) {
    console.error("Lỗi:", err);
    error.value = "Không thể tải dữ liệu từ Server.";
    loading.value = false;
  }
};

onMounted(() => {
  fetchProducts();
});
</script>

<template>
  <section class="py-12 scroll-mt-20 bg-gray-50" id="products">
    <main class="container mx-auto px-4">
      
      <header class="flex justify-between items-center mb-8">
        <h2 class="md:text-3xl text-2xl font-bold text-amber-900">
            Sản Phẩm Nổi Bật
            <span v-if="loading" class="text-sm font-normal text-gray-500 ml-2">(Đang tải...)</span>
            <span v-if="error" class="text-sm font-normal text-red-500 ml-2">({{ error }})</span>
        </h2>
        
        <nav class="flex space-x-4" aria-label="Product carousel controls">
          <button @click="prevSlide" class="p-2 rounded-full bg-white shadow-md hover:bg-pink-100 text-pink-600 transition-colors">
            <Icon icon="line-md:arrow-small-left" width="24" height="24"/>
          </button>
          <button @click="nextSlide" class="p-2 rounded-full bg-white shadow-md hover:bg-pink-100 text-pink-600 transition-colors">
            <Icon icon="line-md:arrow-small-right" width="24" height="24"/>
          </button>
        </nav>
      </header>

      <section class="relative overflow-hidden min-h-[400px]">
        
        <div v-if="!loading && products.length === 0" class="text-center text-gray-500 py-10">
            Chưa có sản phẩm nào.
        </div>

        <ul v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          <li v-for="product in visibleProducts" :key="product.id" class="bg-white rounded-lg shadow-md overflow-hidden transition-all duration-300 hover:scale-105">
            <article>
              <figure class="relative">
                <img :src="product.image" :alt="product.name" class="w-full h-48 object-cover"/>
                
                <figcaption v-if="product.discount > 0" class="absolute top-3 right-3 bg-red-500 text-white text-xs font-bold px-2 py-1 rounded-full">
                    {{ product.discount }}%
                </figcaption>
                
                <button class="absolute top-3 left-3 p-2 bg-white rounded-full shadow-md hover:bg-pink-100 text-gray-700">
                  <Icon icon="mdi:heart" width="18" height="18"/>
                </button>
              </figure>

              <section class="p-4">
                <div class="flex items-center mb-2">
                  <Icon v-for="i in 5" :key="i" icon="line-md:star-alt-filled" width="16" height="16" :class="i <= Math.floor(product.rating) ? 'text-yellow-400':'text-gray-300'"/>
                  <span class="text-sm text-gray-500 ml-1">({{ product.rating }})</span>
                </div>

                <h3 class="text-lg font-semibold text-gray-800 mb-1 truncate" :title="product.name">{{ product.name }}</h3>
                
                <footer class="flex items-center justify-between mt-3">
                  <div>
                    <span class="text-lg font-bold text-pink-600">
                        {{ new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.price) }}
                    </span>
                  </div>
                  <button class="p-2 bg-pink-950 rounded-full text-white hover:bg-pink-700 transition-colors">
                    <Icon icon="icon-park-solid:shopping" width="18" height="18"/>
                  </button>
                </footer>
              </section>
            </article>
          </li>
        </ul>
      </section>

      <nav v-if="totalSlides > 1" class="flex justify-center mt-6 space-x-2">
        <button 
            v-for="index in totalSlides" 
            :key="index" 
            @click="currentSlide = index - 1" 
            :class="['w-3 h-3 rounded-full transition-colors', currentSlide === index - 1 ? 'bg-pink-950':'bg-gray-300']">
        </button>
      </nav>

    </main>
  </section>
</template>

<style scoped>
</style>