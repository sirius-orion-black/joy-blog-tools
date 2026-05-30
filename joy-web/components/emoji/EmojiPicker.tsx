'use client';

import React, { useRef, useState, useEffect, useCallback } from 'react';
import { EMOJI_DATA } from '@/lib/emoji';

import './index.css'

interface EmojiPickerProps {
  onInsert: (emoji: string) => void;
}

//表情
const EmojiPicker: React.FC<EmojiPickerProps> = ({ onInsert }) => {
  const [open, setOpen] = useState(false);
  const [activeCategory, setActiveCategory] = useState(0);
  const panelRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLButtonElement>(null);

  // 点外部关闭
  useEffect(() => {
    if (!open) return;
    const handler = (e: MouseEvent) => {
      if (panelRef.current && !panelRef.current.contains(e.target as Node)) {
        setOpen(false);
      }
    };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, [open]);

  // ESC 关闭
  useEffect(() => {
    if (!open) return;
    const handler = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        setOpen(false);
        triggerRef.current?.focus();
      }
    };
    document.addEventListener('keydown', handler);
    return () => document.removeEventListener('keydown', handler);
  }, [open]);

  const handleSelect = useCallback(
    (emoji: string) => {
      onInsert(emoji);
      setOpen(false);
      triggerRef.current?.focus();
    },
    [onInsert],
  );

  return (
    <div className="emoji-picker-wrapper" ref={panelRef}>
      <button
        ref={triggerRef}
        type="button"
        className="emoji-trigger"
        onClick={() => setOpen((prev) => !prev)}
        aria-label="插入表情"
        aria-expanded={open}
      >
        😊
      </button>

      {open && (
        <div className="emoji-panel">
          {/* 分类标签 */}
          <div className="emoji-category-tabs">
            {EMOJI_DATA.map((cat, i) => (
              <button
                key={cat.name}
                type="button"
                className={`emoji-category-tab ${i === activeCategory ? 'active' : ''}`}
                onClick={() => setActiveCategory(i)}
                title={cat.name}
              >
                {cat.icon}
              </button>
            ))}
          </div>

          {/* 表情网格 */}
          <div className="emoji-grid">
            {EMOJI_DATA[activeCategory].list.map((emoji) => (
              <button
                key={emoji}
                type="button"
                className="emoji-item"
                onClick={() => handleSelect(emoji)}
                aria-label={emoji}
              >
                {emoji}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default EmojiPicker;
