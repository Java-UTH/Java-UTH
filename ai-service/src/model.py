import torch.nn as nn
from torchvision import models

class MultiLabelRetinaModel(nn.Module):
    def __init__(self, num_classes=7):
        super(MultiLabelRetinaModel, self).__init__()
        # Sử dụng cú pháp mới để tránh lỗi pretrained
        self.resnet = models.resnet50(weights=None)
        in_features = self.resnet.fc.in_features
        self.resnet.fc = nn.Linear(in_features, num_classes)

    def forward(self, x):
        return self.resnet(x)