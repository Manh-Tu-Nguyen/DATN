<template>
    <section class="py-8 bg-gray-50">
        <div class="container mx-auto px-6">
            <ul class="flex gap-6 overflow-x-auto snap-x scroll-smooth pb-4 md:grid md:grid-cols-3 md:overflow-visible">
                <li v-for="(item, index) in collections" :key="index" class="min-w-[80%] md:min-w-0 snap-center">
                    <div class="relative h-[400px] rounded-2xl bg-cover bg-center flex flex-col justify-end p-6 text-white shadow-lg transition hover:scale-[1.02]]"
                        :style="{ backgroundImage: `url(${item.image})` }">
                        <div class="absolute inset-0 bg-black/40 rounded-2xl"></div>

                        <div class="relative z-10">
                            <h3 class="text-3xl font-semibold mb-3">{{ item.title }}</h3>
                            <a href="#"
                                class="inline-flex items-center gap-2 bg-white/90 text-gray-800 hover:bg-white px-4 py-2 rounded-full font-medium transition">
                                <span>Xem tất cả</span>
                                <ion-icon name="arrow-forward-outline" aria-hidden="true"></ion-icon>
                            </a>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </section>
</template>

<script setup>
import collection1 from '../../assets/images/collection-1.jpg'
import collection2 from '../../assets/images/collection-2.jpg'
import collection3 from '../../assets/images/collection-3.jpg'

const collections = [
    { title: 'Giày nam', image: collection1 },
    { title: 'Giày nữ', image: collection2 },
    { title: 'Giày trẻ em', image: collection3 },
]
</script>
