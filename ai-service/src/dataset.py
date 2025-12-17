import os

import pandas as pd
import torch
from PIL import Image
from torch.utils.data import Dataset


class RetinaDataset(Dataset):
    def __init__(self, csv_path, image_dir, transform=None):
        """
        csv_path: đường dẫn tới file csv (train_split.csv hoặc val.csv)
        image_dir: thư mục chứa ảnh
        transform: torchvision transform
        """
        self.df = pd.read_csv(csv_path)
        self.image_dir = image_dir
        self.transform = transform

        # Cột nhãn = tất cả cột trừ filename
        self.label_cols = self.df.columns[1:].tolist()

    def __len__(self):
        return len(self.df)

    def __getitem__(self, idx):
        row = self.df.iloc[idx]

        # Đường dẫn ảnh
        img_path = os.path.join(self.image_dir, row["filename"])
        image = Image.open(img_path).convert("RGB")

        if self.transform:
            image = self.transform(image)

        # Label dạng tensor float (multi-label)
        labels = row[self.label_cols].astype(float).values
        label = torch.tensor(labels, dtype=torch.float32)

        return image, label
