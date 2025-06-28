// CCTV页面清理插件
// 移除导航栏和顶部横幅，提供更干净的观看体验
// 并将页面向右和下移动20px

(function() {
    console.log('CCTV清理插件开始执行...');
    
    // 要删除的CSS类列表
    const classesToRemove = [
        'nav_wrapper_bg',
        'newtopbz',
        'header_nav',
        'newtopbzTV',
        'vspace'
    ];
    
    // 要删除的ID列表（可选）
    const idsToRemove = [
        'topNavigation',
        'header',
        'nav'
    ];
    
    // 删除包含指定类的元素
    function removeElementsByClass(className) {
        const elements = document.getElementsByClassName(className);
        let removedCount = 0;
        
        // 从后往前删除，避免动态集合的问题
        for (let i = elements.length - 1; i >= 0; i--) {
            try {
                elements[i].remove();
                removedCount++;
                console.log(`移除了类为 "${className}" 的元素`);
            } catch (e) {
                console.warn(`移除类 "${className}" 的元素时出错:`, e);
            }
        }
        
        return removedCount;
    }
    
    // 删除包含指定ID的元素
    function removeElementsById(id) {
        const element = document.getElementById(id);
        if (element) {
            try {
                element.remove();
                console.log(`移除了ID为 "${id}" 的元素`);
                return 1;
            } catch (e) {
                console.warn(`移除ID "${id}" 的元素时出错:`, e);
            }
        }
        return 0;
    }
    
    // 使用CSS选择器删除复合类的元素
    function removeElementsByCompoundSelector() {
        const selectors = [
            '.nav_wrapper_bg.newtopbz',
            '.header_nav.newtopbzTV',
            '[class*="nav_wrapper_bg"]',
            '[class*="newtopbz"]',
            '[class*="header_nav"]',
            '[class*="newtopbzTV"]'
        ];
        
        let totalRemoved = 0;
        
        selectors.forEach(selector => {
            try {
                const elements = document.querySelectorAll(selector);
                elements.forEach(element => {
                    element.remove();
                    totalRemoved++;
                    console.log(`移除了选择器 "${selector}" 匹配的元素`);
                });
            } catch (e) {
                console.warn(`使用选择器 "${selector}" 时出错:`, e);
            }
        });
        
        return totalRemoved;
    }
    
    // 隐藏可能包含广告或干扰内容的元素
    function hideDistractionElements() {
        const selectorsToHide = [
            '.ad',
            '.advertisement',
            '.banner',
            '[class*="ad"]',
            '[id*="ad"]',
            '.popup',
            '.modal'
        ];
        
        let hiddenCount = 0;
        
        selectorsToHide.forEach(selector => {
            try {
                const elements = document.querySelectorAll(selector);
                elements.forEach(element => {
                    element.style.display = 'none';
                    hiddenCount++;
                });
            } catch (e) {
                console.warn(`隐藏选择器 "${selector}" 时出错:`, e);
            }
        });
        
        if (hiddenCount > 0) {
            console.log(`隐藏了 ${hiddenCount} 个干扰元素`);
        }
        
        return hiddenCount;
    }
    
    // 设置playingVideo元素的margin-left为0
    function adjustPlayingVideoLayout() {
        try {
            const playingVideo = document.querySelector('.playingVideo');
            if (playingVideo) {
                playingVideo.style.marginLeft = '0';
                console.log('已设置.playingVideo的margin-left为0');
                return true;
            } else {
                console.log('未找到.playingVideo元素');
                return false;
            }
        } catch (e) {
            console.warn('调整playingVideo布局时出错:', e);
            return false;
        }
    }
    
    // 将页面向右和下移动10px，并放大1.01倍
    function movePageOffset() {
        console.log('开始调整页面布局...');
        
        try {
            // 居中视频播放器
            console.log('视频居中失败，使用备用布局调整');
                
            // 设置页面缩放为1.01倍
            document.body.style.zoom = '1.02';
            // 备用方案：如果zoom不支持，使用transform
            document.body.style.transform = 'scale(1.02)';
            
            console.log('页面已放大至1.01倍');
            
        } catch (e) {
            console.error('调整页面布局时出错:', e);
        }
    }

    // 主清理函数
    function cleanPage() {
        console.log('开始清理CCTV页面...');
        
        let totalRemoved = 0;
        
        // 1. 删除指定类的元素
        classesToRemove.forEach(className => {
            totalRemoved += removeElementsByClass(className);
        });
        
        // 2. 删除指定ID的元素
        idsToRemove.forEach(id => {
            totalRemoved += removeElementsById(id);
        });
        
        // 3. 使用复合选择器删除元素
        totalRemoved += removeElementsByCompoundSelector();
        
        // 4. 隐藏干扰元素
        hideDistractionElements();
        
        // 5. 调整playingVideo布局
        adjustPlayingVideoLayout();

        console.log(`CCTV清理插件执行完成，共移除了 ${totalRemoved} 个元素`);
        
        return totalRemoved;
    }
    
    // 延迟执行，确保页面内容已加载
    function executeWithDelay() {
        setTimeout(() => {
            cleanPage();
            
            // 应用页面偏移
            movePageOffset();
            
            // 如果页面有动态内容，再次执行清理
            setTimeout(cleanPage, 2000);
            setTimeout(cleanPage, 5000);
        }, 1000);
    }
    
    // 检查页面是否已完全加载
    if (document.readyState === 'complete') {
        executeWithDelay();
    } else {
        // 等待页面加载完成
        window.addEventListener('load', executeWithDelay);
        
        // 如果DOM已就绪但资源未完全加载
        if (document.readyState === 'interactive') {
            executeWithDelay();
        }
    }
    
    // 监听DOM变化，处理动态添加的元素
    const observer = new MutationObserver(function(mutations) {
        let shouldClean = false;
        
        mutations.forEach(function(mutation) {
            if (mutation.type === 'childList' && mutation.addedNodes.length > 0) {
                mutation.addedNodes.forEach(function(node) {
                    if (node.nodeType === 1) { // Element node
                        // 检查新添加的元素是否包含需要清理的类
                        classesToRemove.forEach(className => {
                            if (node.classList && node.classList.contains(className)) {
                                shouldClean = true;
                            }
                        });
                    }
                });
            }
        });
        
        if (shouldClean) {
            setTimeout(cleanPage, 500);
        }
    });
    
    // 开始观察DOM变化
    observer.observe(document.body || document.documentElement, {
        childList: true,
        subtree: true
    });
    
    console.log('CCTV清理插件初始化完成');
})(); 